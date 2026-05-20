package io.higgus.lab.module.bulong.service.common;

import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 产品缓存服务
 *
 * 职责：封装产品相关的缓存操作
 * 优点：业务代码与 Redis 解耦，便于后续切换缓存实现
 *
 * 空值缓存策略说明：
 * - 对于不存在的数据，也写入缓存（使用特殊标记）
 * - 空值缓存 TTL 较短（默认 60 秒），防止数据真正存在时无法及时更新
 * - 通过 "NULL:" 前缀区分空值和正常缓存
 */
@Slf4j
@Service
public class ProductCacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== 常量定义 ====================

    /** 空值缓存前缀，区分空值和正常缓存 */
    private static final String NULL_PREFIX = "NULL:";

    /** 空值缓存默认 TTL：60 秒 */
    private static final long DEFAULT_NULL_TTL = 60;

    // ==================== 单个产品缓存 ====================

    /**
     * 获取产品缓存
     *
     * @param productCode 产品编号
     * @return 产品DO 或 null（未命中）
     */
    public ProductDO getByCode(String productCode) {
        String key = RedisKeys.getProductByCodeKey(productCode);
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value instanceof ProductDO) {
                log.debug("【缓存命中】产品编号: {}", productCode);
                return (ProductDO) value;
            }
        } catch (Exception e) {
            log.warn("【缓存异常】读取产品缓存失败: {}", productCode, e);
        }
        log.debug("【缓存未命中】产品编号: {}", productCode);
        return null;
    }

    /**
     * 设置产品缓存（使用默认TTL）
     *
     * @param productDO 产品DO
     */
    public void setByCode(ProductDO productDO) {
        setByCode(productDO, RedisKeys.TTL_MEDIUM, TimeUnit.SECONDS);
    }

    /**
     * 设置产品缓存（自定义TTL）
     *
     * @param productDO 产品DO
     * @param ttl 过期时间
     * @param unit 时间单位
     */
    public void setByCode(ProductDO productDO, long ttl, TimeUnit unit) {
        if (productDO == null || productDO.getProductCode() == null) {
            return;
        }
        String key = RedisKeys.getProductByCodeKey(productDO.getProductCode());
        try {
            redisTemplate.opsForValue().set(key, productDO, ttl, unit);
            log.debug("【缓存写入】产品编号: {}, TTL: {} {}", productDO.getProductCode(), ttl, unit);
        } catch (Exception e) {
            log.warn("【缓存异常】写入产品缓存失败: {}", productDO.getProductCode(), e);
        }
    }

    /**
     * 删除产品缓存
     *
     * @param productCode 产品编号
     */
    public void evictByCode(String productCode) {
        String key = RedisKeys.getProductByCodeKey(productCode);
        try {
            Boolean deleted = redisTemplate.delete(key);
            log.info("【缓存删除】产品编号: {}, 结果: {}", productCode, deleted);
        } catch (Exception e) {
            log.warn("【缓存异常】删除产品缓存失败: {}", productCode, e);
        }
    }

    /**
     * 获取产品缓存的剩余TTL
     *
     * @param productCode 产品编号
     * @return 剩余秒数，-1表示永久，-2表示不存在
     */
    public Long getTtl(String productCode) {
        String key = RedisKeys.getProductByCodeKey(productCode);
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("【缓存异常】获取产品缓存TTL失败: {}", productCode, e);
            return -2L;
        }
    }

    // ==================== 空值缓存（防止缓存穿透） ====================

    /**
     * 设置空值缓存（用于防止缓存穿透）
     * <p>
     * 当数据库中不存在某产品时，将空值写入缓存，避免重复查询数据库
     *
     * @param productCode 产品编号
     */
    public void setNullValue(String productCode) {
        setNullValue(productCode, DEFAULT_NULL_TTL, TimeUnit.SECONDS);
    }

    /**
     * 设置空值缓存（自定义 TTL）
     *
     * @param productCode 产品编号
     * @param ttl 过期时间
     * @param unit 时间单位
     */
    public void setNullValue(String productCode, long ttl, TimeUnit unit) {
        if (productCode == null) {
            return;
        }
        String key = RedisKeys.getProductByCodeKey(productCode);
        try {
            // 使用特殊前缀标记空值
            redisTemplate.opsForValue().set(NULL_PREFIX + key, "", ttl, unit);
            log.debug("【空值缓存】已写入: {}, TTL: {} {}", productCode, ttl, unit);
        } catch (Exception e) {
            log.warn("【空值缓存异常】写入失败: {}", productCode, e);
        }
    }

    /**
     * 检查是否为缓存的空值
     *
     * @param productCode 产品编号
     * @return true=是空值缓存，false=正常缓存或不存在
     */
    public boolean isNullValue(String productCode) {
        String key = RedisKeys.getProductByCodeKey(productCode);
        try {
            Object value = redisTemplate.opsForValue().get(NULL_PREFIX + key);
            return value != null && "".equals(value.toString());
        } catch (Exception e) {
            log.warn("【空值检查异常】: {}", productCode, e);
            return false;
        }
    }

    /**
     * 检查缓存是否存在（包括空值缓存）
     *
     * @param productCode 产品编号
     * @return true=存在（可能是正常值或空值），false=不存在
     */
    public boolean exists(String productCode) {
        String key = RedisKeys.getProductByCodeKey(productCode);
        try {
            // 检查正常缓存
            Boolean hasKey = redisTemplate.hasKey(key);
            if (Boolean.TRUE.equals(hasKey)) {
                return true;
            }
            // 检查空值缓存
            return Boolean.TRUE.equals(redisTemplate.hasKey(NULL_PREFIX + key));
        } catch (Exception e) {
            log.warn("【缓存检查异常】: {}", productCode, e);
            return false;
        }
    }

    // ==================== 产品列表缓存 ====================

    /**
     * 获取产品列表缓存
     */
    @SuppressWarnings("unchecked")
    public List<ProductDO> getList(String cacheKey) {
        try {
            Object value = redisTemplate.opsForValue().get(cacheKey);
            if (value instanceof List) {
                log.debug("【缓存命中】产品列表: {}", cacheKey);
                return (List<ProductDO>) value;
            }
        } catch (Exception e) {
            log.warn("【缓存异常】读取产品列表缓存失败: {}", cacheKey, e);
        }
        return null;
    }

    /**
     * 设置产品列表缓存
     */
    public void setList(String cacheKey, List<ProductDO> productList) {
        setList(cacheKey, productList, RedisKeys.TTL_SHORT, TimeUnit.SECONDS);
    }

    public void setList(String cacheKey, List<ProductDO> productList, long ttl, TimeUnit unit) {
        if (productList == null) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(cacheKey, productList, ttl, unit);
            log.debug("【缓存写入】产品列表: {}, 数量: {}", cacheKey, productList.size());
        } catch (Exception e) {
            log.warn("【缓存异常】写入产品列表缓存失败: {}", cacheKey, e);
        }
    }

    /**
     * 删除产品列表缓存
     */
    public void evictList(String cacheKey) {
        try {
            redisTemplate.delete(cacheKey);
            log.info("【缓存删除】产品列表: {}", cacheKey);
        } catch (Exception e) {
            log.warn("【缓存异常】删除产品列表缓存失败: {}", cacheKey, e);
        }
    }
}
