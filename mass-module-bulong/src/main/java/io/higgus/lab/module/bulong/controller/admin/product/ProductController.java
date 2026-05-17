package io.higgus.lab.module.bulong.controller.admin.product;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import io.higgus.lab.module.bulong.controller.admin.product.vo.*;
import io.higgus.lab.module.bulong.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "布隆后台 - 产品管理")
@RestController
@RequestMapping("/admin/bulong/product")
@Validated
public class ProductController {

    @Resource
    private ProductService productService;

    @Operation(summary = "创建产品")
    @PostMapping("/create")
    public CommonResult<Long> createProduct(@RequestBody @Valid ProductCreateReqVO createReqVO) {
        Long id = productService.createProduct(createReqVO);
        return success(id);
    }

    @Operation(summary = "更新产品")
    @PutMapping("/update")
    public CommonResult<Boolean> updateProduct(@RequestBody @Valid ProductUpdateReqVO updateReqVO) {
        productService.updateProduct(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除产品")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteProduct(@Parameter(name = "id", description = "产品ID", required = true) @RequestParam Long id) {
        productService.deleteProduct(id);
        return success(true);
    }

    @Operation(summary = "获取产品（根据ID）")
    @GetMapping("/get")
    public CommonResult<ProductRespVO> getProduct(@Parameter(name = "id", description = "产品ID", required = true) @RequestParam Long id) {
        ProductRespVO product = productService.getProduct(id);
        return success(product);
    }

    @Operation(summary = "获取产品（根据编号）")
    @GetMapping("/get-by-code")
    public CommonResult<ProductRespVO> getProductByCode(@Parameter(name = "productCode", description = "产品编号", required = true) @RequestParam String productCode) {
        ProductRespVO product = productService.getProductByCode(productCode);
        return success(product);
    }

    @Operation(summary = "获取产品列表")
    @GetMapping("/list")
    public CommonResult<List<ProductRespVO>> getProductList(ProductListReqVO listReqVO) {
        List<ProductRespVO> list = productService.getProductList(listReqVO);
        return success(list);
    }

    @Operation(summary = "获取产品分页列表")
    @GetMapping("/page")
    public CommonResult<PageResult<ProductRespVO>> getProductPage(ProductPageReqVO pageReqVO) {
        PageResult<ProductRespVO> result = productService.getProductPage(pageReqVO);
        return success(result);
    }
}
