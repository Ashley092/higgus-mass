package io.higgus.lab.module.erp.admin.convert.product;


import io.higgus.lab.mass.framework.common.util.object.BeanUtils;
import io.higgus.lab.module.erp.admin.controller.vo.ProductRespVO;
import io.higgus.lab.module.erp.admin.dal.dataobject.product.ProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductConvert {

    ProductConvert INSTANCE = Mappers.getMapper(ProductConvert.class);

    default ProductRespVO convert(ProductDO product) {
        ProductRespVO productRespVO = BeanUtils.toBean( product, ProductRespVO.class);
        return productRespVO;
    }
}
