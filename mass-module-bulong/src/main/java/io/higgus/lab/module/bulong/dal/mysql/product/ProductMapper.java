package io.higgus.lab.module.bulong.dal.mysql.product;

import io.higgus.lab.framework.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品表 Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapperX<ProductDO> {

    /**
     * 根据产品编号查询
     */
    default ProductDO getByProductCode(String productCode) {
        return selectOne(ProductDO::getProductCode, productCode);
    }
}
