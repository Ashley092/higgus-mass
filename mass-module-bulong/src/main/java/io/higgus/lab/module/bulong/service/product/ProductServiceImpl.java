package io.higgus.lab.module.bulong.service.product;

import io.higgus.lab.module.bulong.controller.admin.product.vo.*;
import io.higgus.lab.module.bulong.convert.ProductConvert;
import io.higgus.lab.module.bulong.dal.dataobject.product.ProductDO;
import io.higgus.lab.module.bulong.dal.mysql.product.ProductMapper;
import io.higgus.lab.module.bulong.service.common.ProductCacheService;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品管理 Service 实现类
 *
 * 缓存策略：
 * - 查询：先查缓存，未命中则查数据库并写入缓存
 * - 新增：直接写数据库，不缓存（新增数据变化频繁）
 * - 更新/删除：更新/删除数据库后，同步清除缓存
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductCacheService productCacheService;

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

        // 【关键】清除该产品的缓存
        productCacheService.evictByCode(existProduct.getProductCode());
        log.info("【缓存同步】产品更新，清除缓存: {}", existProduct.getProductCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        // 先获取产品编号，用于清除缓存
        ProductDO product = productMapper.selectById(id);
        if (product != null) {
            productCacheService.evictByCode(product.getProductCode());
            log.info("【缓存同步】产品删除，清除缓存: {}", product.getProductCode());
        }
        productMapper.deleteById(id);
    }

    @Override
    public ProductRespVO getProduct(Long id) {
        ProductDO productDO = productMapper.selectById(id);
        return productConvert.toProductRespVO(productDO);
    }

    @Override
    public ProductRespVO getProductByCode(String productCode) {
        // ========== 第一步：查询缓存 ==========
        ProductDO cachedProduct = productCacheService.getByCode(productCode);
        if (cachedProduct != null) {
            log.info("【查询】产品编号: {} - 来源: 缓存", productCode);
            return productConvert.toProductRespVO(cachedProduct);
        }

        // ========== 第二步：检查空值缓存（防止缓存穿透） ==========
        if (productCacheService.isNullValue(productCode)) {
            log.info("【查询】产品编号: {} - 来源: 空值缓存（快速返回）", productCode);
            return null;
        }

        // ========== 第三步：缓存未命中，查询数据库 ==========
        log.info("【查询】产品编号: {} - 来源: 数据库", productCode);
        ProductDO productDO = productMapper.getByProductCode(productCode);

        // ========== 第四步：写入缓存（防止穿透） ==========
        if (productDO != null) {
            // 正常数据，写入正常缓存
            productCacheService.setByCode(productDO);
            log.debug("【缓存】产品已写入缓存: {}", productCode);
        } else {
            // 数据不存在，写入空值缓存（短TTL，防止穿透）
            productCacheService.setNullValue(productCode);
            log.info("【缓存】空值已写入缓存，防止穿透: {}", productCode);
        }

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
