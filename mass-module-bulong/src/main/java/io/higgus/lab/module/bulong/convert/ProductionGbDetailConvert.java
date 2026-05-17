package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionGbDetailDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 梳栉工艺明细对象转换器
 */
@Mapper
public interface ProductionGbDetailConvert {

    ProductionGbDetailConvert INSTANCE = Mappers.getMapper(ProductionGbDetailConvert.class);

    /**
     * CreateReqVO -> ProductionGbDetailDO
     */
    ProductionGbDetailDO toProductionGbDetailDO(GbDetailCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> ProductionGbDetailDO
     */
    ProductionGbDetailDO toProductionGbDetailDO(GbDetailUpdateReqVO updateReqVO);

    /**
     * ProductionGbDetailDO -> GbDetailRespVO
     */
    GbDetailRespVO toGbDetailRespVO(ProductionGbDetailDO detailDO);

    /**
     * 批量转换
     */
    List<GbDetailRespVO> toGbDetailRespVOList(List<ProductionGbDetailDO> detailDOList);
}
