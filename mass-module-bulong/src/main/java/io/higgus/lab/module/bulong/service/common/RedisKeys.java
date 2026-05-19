package io.higgus.lab.module.bulong.service.common;

/**
 * Redis Key 常量定义
 *
 * 命名规范：模块:实体:操作
 * 例如：bulong:product:code:xxx 表示产品表按编号查询
 */
public interface RedisKeys {

    // ==================== 产品模块 ====================
    String PRODUCT_PREFIX = "bulong:product:";

    /**
     * 产品缓存 Key
     * 用法：bulong:product:code:P001
     */
    String PRODUCT_CODE = PRODUCT_PREFIX + "code:";

    /**
     * 产品列表缓存 Key
     */
    String PRODUCT_LIST = PRODUCT_PREFIX + "list";

    // ==================== 生产记录模块 ====================
    String PRODUCTION_PREFIX = "bulong:production:";

    /**
     * 生产记录缓存 Key
     * 用法：bulong:production:record:PR20250101120000ABC
     */
    String PRODUCTION_RECORD = PRODUCTION_PREFIX + "record:";

    /**
     * 生产记录列表缓存 Key
     */
    String PRODUCTION_LIST = PRODUCTION_PREFIX + "list";

    /**
     * 根据产品编号获取生产记录列表的缓存
     */
    String PRODUCTION_LIST_BY_PRODUCT = PRODUCTION_PREFIX + "list:product:";

    // ==================== 缓存过期时间 ====================

    /** 短时间缓存：5分钟 */
    long TTL_SHORT = 5 * 60L;

    /** 中等缓存：30分钟 */
    long TTL_MEDIUM = 30 * 60L;

    /** 长时间缓存：2小时 */
    long TTL_LONG = 2 * 60 * 60L;

    // ==================== Key 构建方法 ====================

    static String getProductByCodeKey(String productCode) {
        return PRODUCT_CODE + productCode;
    }

    static String getProductionRecordKey(String recordId) {
        return PRODUCTION_RECORD + recordId;
    }

    static String getProductionListByProductKey(String productCode) {
        return PRODUCTION_LIST_BY_PRODUCT + productCode;
    }
}
