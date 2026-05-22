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

    // ========== 机台管理 1-010-004-xxx ==========

    /**
     * 机台不存在
     */
    int MACHINE_NOT_EXISTS = 1010004001;

    /**
     * 机台编码已存在
     */
    int MACHINE_CODE_DUPLICATE = 1010004002;

    // ========== 生产计划管理 1-010-005-xxx ==========

    /**
     * 生产计划不存在
     */
    int PRODUCTION_PLAN_NOT_EXISTS = 1010005001;

    /**
     * 计划编码已存在
     */
    int PLAN_CODE_DUPLICATE = 1010005002;

    /**
     * 计划状态不允许该操作
     */
    int PLAN_STATUS_NOT_ALLOWED = 1010005003;

    /**
     * 计划未分配机台
     */
    int PLAN_NO_MACHINE_ASSIGNED = 1010005004;

    // ========== 计划-机台关联管理 1-010-006-xxx ==========

    /**
     * 计划-机台关联不存在
     */
    int PLAN_MACHINE_NOT_EXISTS = 1010006001;

    /**
     * 计划-机台关联已存在
     */
    int PLAN_MACHINE_DUPLICATE = 1010006002;

    // ========== 产品工艺管理 1-010-007-xxx ==========

    /**
     * 产品工艺不存在
     */
    int PRODUCT_PROCESS_NOT_EXISTS = 1010007001;

    /**
     * 工艺编号已存在
     */
    int PROCESS_CODE_DUPLICATE = 1010007002;

    /**
     * 梳栉编号已存在
     */
    int GB_INDEX_DUPLICATE = 1010007003;

    // ========== 机台采集数据管理 1-010-008-xxx ==========

    /**
     * 采集数据不存在
     */
    int MACHINE_PRODUCTION_NOT_EXISTS = 1010008001;
}
