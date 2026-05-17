package io.higgus.lab.module.bulong.service.product;

import io.higgus.lab.module.bulong.controller.admin.product.vo.*;
import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;

import java.util.List;

/**
 * 产品管理 Service 接口
 */
public interface ProductService {

    /**
     * 创建产品
     */
    Long createProduct(ProductCreateReqVO createReqVO);

    /**
     * 更新产品
     */
    void updateProduct(ProductUpdateReqVO updateReqVO);

    /**
     * 删除产品
     */
    void deleteProduct(Long id);

    /**
     * 根据ID获取产品
     */
    ProductRespVO getProduct(Long id);

    /**
     * 根据产品编号获取产品
     */
    ProductRespVO getProductByCode(String productCode);

    /**
     * 获取产品列表
     */
    List<ProductRespVO> getProductList(ProductListReqVO listReqVO);

    /**
     * 获取产品分页
     */
    io.higgus.lab.mass.framework.common.util.object.PageResult<ProductRespVO> getProductPage(ProductPageReqVO pageReqVO);
}
