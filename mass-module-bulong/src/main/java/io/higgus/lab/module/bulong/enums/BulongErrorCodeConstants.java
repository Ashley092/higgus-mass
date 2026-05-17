package io.higgus.lab.module.bulong.enums;

/**
 * Bulong 模块错误码枚举类
 * 错误码区间: 1-010-000-000 ~ 1-010-999-999
 */
public interface BulongErrorCodeConstants {

    // ========== 产品管理 1-010-001-xxx ==========

    /**
     * 产品不存在
     */
    int PRODUCT_NOT_EXISTS = 1010001001;

    /**
     * 产品编号已存在
     */
    int PRODUCT_CODE_DUPLICATE = 1010001002;

    // ========== 生产记录管理 1-010-002-xxx ==========

    /**
     * 生产记录不存在
     */
    int PRODUCTION_RECORD_NOT_EXISTS = 1010002001;

    /**
     * 生产记录序号已存在
     */
    int PRODUCTION_RECORD_ID_DUPLICATE = 1010002002;

    // ========== 梳栉工艺明细管理 1-010-003-xxx ==========

    /**
     * 梳栉工艺明细不存在
     */
    int GB_DETAIL_NOT_EXISTS = 1010003001;

    /**
     * 梳栉工艺明细重复
     */
    int GB_DETAIL_DUPLICATE = 1010003002;
}
