package io.higgus.lab.module.bulong.service.production;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.mass.framework.common.util.object.PageResult;

import java.util.List;

/**
 * 生产记录管理 Service 接口
 */
public interface ProductionRecordService {

    /**
     * 创建生产记录
     */
    String createRecord(ProductionRecordCreateReqVO createReqVO);

    /**
     * 更新生产记录
     */
    void updateRecord(ProductionRecordUpdateReqVO updateReqVO);

    /**
     * 删除生产记录
     */
    void deleteRecord(String recordId);

    /**
     * 根据ID获取生产记录
     */
    ProductionRecordRespVO getRecord(String recordId);

    /**
     * 获取生产记录列表
     */
    List<ProductionRecordRespVO> getRecordList(ProductionRecordListReqVO listReqVO);

    /**
     * 获取生产记录分页
     */
    PageResult<ProductionRecordRespVO> getRecordPage(ProductionRecordPageReqVO pageReqVO);

    /**
     * 根据产品编号获取生产记录
     */
    List<ProductionRecordRespVO> getRecordListByProductCode(String productCode);
}
