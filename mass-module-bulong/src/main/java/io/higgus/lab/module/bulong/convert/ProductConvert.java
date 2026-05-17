package io.higgus.lab.module.bulong.convert;

import io.higgus.lab.module.bulong.controller.admin.product.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 产品管理对象转换器
 */
@Mapper
public interface ProductConvert {

    ProductConvert INSTANCE = Mappers.getMapper(ProductConvert.class);

    /**
     * CreateReqVO -> ProductDO
     */
    ProductDO toProductDO(ProductCreateReqVO createReqVO);

    /**
     * UpdateReqVO -> ProductDO
     */
    ProductDO toProductDO(ProductUpdateReqVO updateReqVO);

    /**
     * ProductDO -> ProductRespVO
     */
    ProductRespVO toProductRespVO(ProductDO productDO);

    /**
     * 批量转换
     */
    List<ProductRespVO> toProductRespVOList(List<ProductDO> productDOList);
}
