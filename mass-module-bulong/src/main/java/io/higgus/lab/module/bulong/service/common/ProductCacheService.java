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
 */
@Slf4j
@Service
public class ProductCacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
     * 判断产品缓存是否存在
     */
    public boolean exists(String productCode) {
        String key = RedisKeys.getProductByCodeKey(productCode);
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.warn("【缓存异常】检查产品缓存存在失败: {}", productCode, e);
            return false;
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
