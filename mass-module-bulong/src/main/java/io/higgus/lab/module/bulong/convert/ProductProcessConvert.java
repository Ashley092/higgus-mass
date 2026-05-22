package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.process.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.process.ProductProcessDO;
import io.higgus.lab.module.bulong.dal.dataobject.process.ProductProcessDetailDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 产品工艺管理对象转换器
 */
@Mapper
public interface ProductProcessConvert {

    ProductProcessConvert INSTANCE = Mappers.getMapper(ProductProcessConvert.class);

    // ==================== ProductProcess ====================

    /**
     * CreateReqVO -> ProductProcessDO
     */
    ProductProcessDO toProductProcessDO(ProductProcessCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> ProductProcessDO
     */
    ProductProcessDO toProductProcessDO(ProductProcessUpdateReqVO updateReqVO);

    /**
     * ProductProcessDO -> ProductProcessRespVO
     */
    ProductProcessRespVO toProductProcessRespVO(ProductProcessDO processDO);

    /**
     * 批量转换
     */
    List<ProductProcessRespVO> toProductProcessRespVOList(List<ProductProcessDO> processDOList);

    // ==================== ProductProcessDetail ====================

    /**
     * CreateReqVO -> ProductProcessDetailDO
     */
    ProductProcessDetailDO toProductProcessDetailDO(ProductProcessDetailCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> ProductProcessDetailDO
     */
    ProductProcessDetailDO toProductProcessDetailDO(ProductProcessDetailUpdateReqVO updateReqVO);

    /**
     * ProductProcessDetailDO -> ProductProcessDetailRespVO
     */
    ProductProcessDetailRespVO toProductProcessDetailRespVO(ProductProcessDetailDO detailDO);

    /**
     * 批量转换
     */
    List<ProductProcessDetailRespVO> toProductProcessDetailRespVOList(List<ProductProcessDetailDO> detailDOList);
}
