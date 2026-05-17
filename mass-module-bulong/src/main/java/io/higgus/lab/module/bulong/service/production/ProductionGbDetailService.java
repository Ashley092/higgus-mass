package io.higgus.lab.module.bulong.service.production;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;

import java.util.List;

/**
 * 梳栉工艺明细 Service 接口
 */
public interface ProductionGbDetailService {

    /**
     * 创建梳栉工艺明细
     */
    Long createGbDetail(GbDetailCreateReqVO createReqVO);

    /**
     * 更新梳栉工艺明细
     */
    void updateGbDetail(GbDetailUpdateReqVO updateReqVO);

    /**
     * 删除梳栉工艺明细
     */
    void deleteGbDetail(Long detailId);

    /**
     * 根据ID获取梳栉工艺明细
     */
    GbDetailRespVO getGbDetail(Long detailId);

    /**
     * 根据生产记录序号获取明细列表
     */
    List<GbDetailRespVO> getGbDetailListByRecordId(String recordId);

    /**
     * 批量创建梳栉工艺明细
     */
    void batchCreateGbDetails(String recordId, List<GbDetailCreateReqVO> createReqVOList);
}
