package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.plan.ProductionPlanDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 生产计划管理对象转换器
 */
@Mapper
public interface ProductionPlanConvert {

    ProductionPlanConvert INSTANCE = Mappers.getMapper(ProductionPlanConvert.class);

    /**
     * CreateReqVO -> ProductionPlanDO
     */
    ProductionPlanDO toProductionPlanDO(ProductionPlanCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> ProductionPlanDO
     */
    ProductionPlanDO toProductionPlanDO(ProductionPlanUpdateReqVO updateReqVO);

    /**
     * ProductionPlanDO -> ProductionPlanRespVO
     */
    ProductionPlanRespVO toProductionPlanRespVO(ProductionPlanDO planDO);

    /**
     * 批量转换
     */
    List<ProductionPlanRespVO> toProductionPlanRespVOList(List<ProductionPlanDO> planDOList);
}
