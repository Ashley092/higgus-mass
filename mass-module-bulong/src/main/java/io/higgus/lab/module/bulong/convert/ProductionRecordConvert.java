package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.production.ProductionRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 生产记录管理对象转换器
 */
@Mapper
public interface ProductionRecordConvert {

    ProductionRecordConvert INSTANCE = Mappers.getMapper(ProductionRecordConvert.class);

    /**
     * CreateReqVO -> ProductionRecordDO
     */
    ProductionRecordDO toProductionRecordDO(ProductionRecordCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> ProductionRecordDO
     */
    ProductionRecordDO toProductionRecordDO(ProductionRecordUpdateReqVO updateReqVO);

    /**
     * ProductionRecordDO -> ProductionRecordRespVO
     */
    ProductionRecordRespVO toProductionRecordRespVO(ProductionRecordDO recordDO);

    /**
     * 批量转换
     */
    List<ProductionRecordRespVO> toProductionRecordRespVOList(List<ProductionRecordDO> recordDOList);
}
