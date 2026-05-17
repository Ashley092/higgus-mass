package io.higgus.lab.module.bulong.service.product;

import io.higgus.lab.module.bulong.controller.admin.product.vo.*;
import io.higgus.lab.module.bulong.convert.ProductConvert;
import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;
import io.higgus.lab.module.bulong.dal.mysql.product.ProductMapper;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品管理 Service 实现类
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    private static final ProductConvert productConvert = ProductConvert.INSTANCE;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProduct(ProductCreateReqVO createReqVO) {
        // 校验产品编号唯一性
        ProductDO existProduct = productMapper.getByProductCode(createReqVO.getProductCode());
        if (existProduct != null) {
            throw new RuntimeException("产品编号已存在");
        }

        // 转换并保存
        ProductDO productDO = productConvert.toProductDO(createReqVO);
        productMapper.insert(productDO);

        return productDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductUpdateReqVO updateReqVO) {
        // 校验产品存在
        ProductDO existProduct = productMapper.selectById(updateReqVO.getId());
        if (existProduct == null) {
            throw new RuntimeException("产品不存在");
        }

        // 转换并更新
        ProductDO productDO = productConvert.toProductDO(updateReqVO);
        productMapper.updateById(productDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public ProductRespVO getProduct(Long id) {
        ProductDO productDO = productMapper.selectById(id);
        return productConvert.toProductRespVO(productDO);
    }

    @Override
    public ProductRespVO getProductByCode(String productCode) {
        ProductDO productDO = productMapper.getByProductCode(productCode);
        return productConvert.toProductRespVO(productDO);
    }

    @Override
    public List<ProductRespVO> getProductList(ProductListReqVO listReqVO) {
        List<ProductDO> productList = productMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductDO>()
                .eq(listReqVO.getIsActive() != null, ProductDO::getIsActive, listReqVO.getIsActive())
        );
        return productConvert.toProductRespVOList(productList);
    }

    @Override
    public PageResult<ProductRespVO> getProductPage(ProductPageReqVO pageReqVO) {
        com.baomidou.mybatisplus.core.metadata.IPage<ProductDO> page = productMapper.selectPage(
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize()),
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductDO>()
                .like(pageReqVO.getProductCode() != null, ProductDO::getProductCode, pageReqVO.getProductCode())
                .like(pageReqVO.getProductName() != null, ProductDO::getProductName, pageReqVO.getProductName())
                .eq(pageReqVO.getIsActive() != null, ProductDO::getIsActive, pageReqVO.getIsActive())
                .orderByDesc(ProductDO::getId)
        );
        PageResult<ProductRespVO> result = new PageResult<>();
        result.setList(productConvert.toProductRespVOList(page.getRecords()));
        result.setTotal(page.getTotal());
        return result;
    }
}
