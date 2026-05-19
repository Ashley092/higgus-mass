package io.higgus.lab.module.bulong.service.common;

import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionRecordDO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 生产记录缓存服务
 *
 * 职责：封装生产记录相关的缓存操作
 */
@Slf4j
@Service
public class ProductionRecordCacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== 单个记录缓存 ====================

    /**
     * 获取生产记录缓存
     */
    public ProductionRecordDO getByRecordId(String recordId) {
        String key = RedisKeys.getProductionRecordKey(recordId);
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value instanceof ProductionRecordDO) {
                log.debug("【缓存命中】生产记录: {}", recordId);
                return (ProductionRecordDO) value;
            }
        } catch (Exception e) {
            log.warn("【缓存异常】读取生产记录缓存失败: {}", recordId, e);
        }
        log.debug("【缓存未命中】生产记录: {}", recordId);
        return null;
    }

    /**
     * 设置生产记录缓存
     */
    public void setByRecordId(ProductionRecordDO recordDO) {
        setByRecordId(recordDO, RedisKeys.TTL_SHORT, TimeUnit.SECONDS);
    }

    public void setByRecordId(ProductionRecordDO recordDO, long ttl, TimeUnit unit) {
        if (recordDO == null || recordDO.getRecordId() == null) {
            return;
        }
        String key = RedisKeys.getProductionRecordKey(recordDO.getRecordId());
        try {
            redisTemplate.opsForValue().set(key, recordDO, ttl, unit);
            log.debug("【缓存写入】生产记录: {}, TTL: {} {}", recordDO.getRecordId(), ttl, unit);
        } catch (Exception e) {
            log.warn("【缓存异常】写入生产记录缓存失败: {}", recordDO.getRecordId(), e);
        }
    }

    /**
     * 删除生产记录缓存
     */
    public void evictByRecordId(String recordId) {
        String key = RedisKeys.getProductionRecordKey(recordId);
        try {
            redisTemplate.delete(key);
            log.info("【缓存删除】生产记录: {}", recordId);
        } catch (Exception e) {
            log.warn("【缓存异常】删除生产记录缓存失败: {}", recordId, e);
        }
    }

    // ==================== 列表缓存（按产品编号） ====================

    /**
     * 获取某个产品的生产记录列表缓存
     */
    @SuppressWarnings("unchecked")
    public List<ProductionRecordDO> getListByProductCode(String productCode) {
        String key = RedisKeys.getProductionListByProductKey(productCode);
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value instanceof List) {
                log.debug("【缓存命中】产品 {} 的生产记录列表", productCode);
                return (List<ProductionRecordDO>) value;
            }
        } catch (Exception e) {
            log.warn("【缓存异常】读取生产记录列表缓存失败: {}", productCode, e);
        }
        return null;
    }

    /**
     * 设置某个产品的生产记录列表缓存
     */
    public void setListByProductCode(String productCode, List<ProductionRecordDO> recordList) {
        if (recordList == null) {
            return;
        }
        String key = RedisKeys.getProductionListByProductKey(productCode);
        try {
            redisTemplate.opsForValue().set(key, recordList, RedisKeys.TTL_SHORT, TimeUnit.SECONDS);
            log.debug("【缓存写入】产品 {} 的生产记录列表，数量: {}", productCode, recordList.size());
        } catch (Exception e) {
            log.warn("【缓存异常】写入生产记录列表缓存失败: {}", productCode, e);
        }
    }

    /**
     * 删除某个产品的生产记录列表缓存
     */
    public void evictListByProductCode(String productCode) {
        String key = RedisKeys.getProductionListByProductKey(productCode);
        try {
            redisTemplate.delete(key);
            log.info("【缓存删除】产品 {} 的生产记录列表", productCode);
        } catch (Exception e) {
            log.warn("【缓存异常】删除生产记录列表缓存失败: {}", productCode, e);
        }
    }
}
