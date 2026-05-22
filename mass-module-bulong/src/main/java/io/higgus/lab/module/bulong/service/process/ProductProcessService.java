package io.higgus.lab.module.bulong.service.process;

import io.higgus.lab.module.bulong.controller.admin.process.vo.*;

import java.util.List;

/**
 * 产品工艺管理 Service 接口
 */
public interface ProductProcessService {

    // ==================== ProductProcess ====================

    /**
     * 创建产品工艺
     */
    Long createProductProcess(ProductProcessCreateReqVO createReqVO);

    /**
     * 更新产品工艺
     */
    void updateProductProcess(ProductProcessUpdateReqVO updateReqVO);

    /**
     * 删除产品工艺
     */
    void deleteProductProcess(Long id);

    /**
     * 根据ID获取产品工艺
     */
    ProductProcessRespVO getProductProcess(Long id);

    /**
     * 根据产品ID获取工艺列表
     */
    List<ProductProcessRespVO> getProductProcessListByProductId(Long productId);

    /**
     * 根据产品ID和机台类型获取工艺
     */
    ProductProcessRespVO getProductProcessByProductAndMachineType(Long productId, String machineType);

    // ==================== ProductProcessDetail ====================

    /**
     * 创建工艺明细
     */
    Long createProcessDetail(ProductProcessDetailCreateReqVO createReqVO);

    /**
     * 更新工艺明细
     */
    void updateProcessDetail(ProductProcessDetailUpdateReqVO updateReqVO);

    /**
     * 删除工艺明细
     */
    void deleteProcessDetail(Long id);

    /**
     * 根据ID获取工艺明细
     */
    ProductProcessDetailRespVO getProcessDetail(Long id);

    /**
     * 根据工艺ID获取明细列表
     */
    List<ProductProcessDetailRespVO> getProcessDetailListByProcessId(Long processId);

    /**
     * 批量创建工艺明细
     */
    void batchCreateProcessDetail(Long processId, List<ProductProcessDetailCreateReqVO> createReqVOList);

    /**
     * 一键填充：从标准工艺复制到生产计划
     */
    void copyProcessToPlan(Long processId, Long planId);
}
