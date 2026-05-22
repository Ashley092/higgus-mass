package io.higgus.lab.module.bulong.controller.admin.process;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.bulong.controller.admin.process.vo.*;
import io.higgus.lab.module.bulong.service.process.ProductProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "布隆后台 - 产品工艺管理")
@RestController
@RequestMapping("/admin/bulong/process")
@Validated
public class ProductProcessController {

    @Resource
    private ProductProcessService productProcessService;

    // ==================== ProductProcess ====================

    @Operation(summary = "创建产品工艺")
    @PostMapping("/create")
    public CommonResult<Long> createProductProcess(@RequestBody @Valid ProductProcessCreateReqVO createReqVO) {
        Long id = productProcessService.createProductProcess(createReqVO);
        return success(id);
    }

    @Operation(summary = "更新产品工艺")
    @PutMapping("/update")
    public CommonResult<Boolean> updateProductProcess(@RequestBody @Valid ProductProcessUpdateReqVO updateReqVO) {
        productProcessService.updateProductProcess(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除产品工艺")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteProductProcess(@Parameter(name = "id", description = "工艺ID", required = true) @RequestParam Long id) {
        productProcessService.deleteProductProcess(id);
        return success(true);
    }

    @Operation(summary = "获取产品工艺")
    @GetMapping("/get")
    public CommonResult<ProductProcessRespVO> getProductProcess(@Parameter(name = "id", description = "工艺ID", required = true) @RequestParam Long id) {
        ProductProcessRespVO resp = productProcessService.getProductProcess(id);
        return success(resp);
    }

    @Operation(summary = "根据产品ID获取工艺列表")
    @GetMapping("/list-by-product")
    public CommonResult<List<ProductProcessRespVO>> getProductProcessListByProductId(
            @Parameter(name = "productId", description = "产品ID", required = true) @RequestParam Long productId) {
        List<ProductProcessRespVO> list = productProcessService.getProductProcessListByProductId(productId);
        return success(list);
    }

    @Operation(summary = "根据产品和机台类型获取工艺")
    @GetMapping("/get-by-product-machine")
    public CommonResult<ProductProcessRespVO> getProductProcessByProductAndMachineType(
            @Parameter(name = "productId", description = "产品ID", required = true) @RequestParam Long productId,
            @Parameter(name = "machineType", description = "机台类型", required = true) @RequestParam String machineType) {
        ProductProcessRespVO resp = productProcessService.getProductProcessByProductAndMachineType(productId, machineType);
        return success(resp);
    }

    // ==================== ProductProcessDetail ====================

    @Operation(summary = "创建工艺明细")
    @PostMapping("/detail/create")
    public CommonResult<Long> createProcessDetail(@RequestBody @Valid ProductProcessDetailCreateReqVO createReqVO) {
        Long id = productProcessService.createProcessDetail(createReqVO);
        return success(id);
    }

    @Operation(summary = "更新工艺明细")
    @PutMapping("/detail/update")
    public CommonResult<Boolean> updateProcessDetail(@RequestBody @Valid ProductProcessDetailUpdateReqVO updateReqVO) {
        productProcessService.updateProcessDetail(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除工艺明细")
    @DeleteMapping("/detail/delete")
    public CommonResult<Boolean> deleteProcessDetail(@Parameter(name = "id", description = "明细ID", required = true) @RequestParam Long id) {
        productProcessService.deleteProcessDetail(id);
        return success(true);
    }

    @Operation(summary = "获取工艺明细")
    @GetMapping("/detail/get")
    public CommonResult<ProductProcessDetailRespVO> getProcessDetail(@Parameter(name = "id", description = "明细ID", required = true) @RequestParam Long id) {
        ProductProcessDetailRespVO resp = productProcessService.getProcessDetail(id);
        return success(resp);
    }

    @Operation(summary = "根据工艺ID获取明细列表")
    @GetMapping("/detail/list-by-process")
    public CommonResult<List<ProductProcessDetailRespVO>> getProcessDetailListByProcessId(
            @Parameter(name = "processId", description = "工艺ID", required = true) @RequestParam Long processId) {
        List<ProductProcessDetailRespVO> list = productProcessService.getProcessDetailListByProcessId(processId);
        return success(list);
    }

    @Operation(summary = "批量创建工艺明细")
    @PostMapping("/detail/batch-create")
    public CommonResult<Boolean> batchCreateProcessDetail(
            @Parameter(name = "processId", description = "工艺ID", required = true) @RequestParam Long processId,
            @RequestBody @Valid List<ProductProcessDetailCreateReqVO> createReqVOList) {
        productProcessService.batchCreateProcessDetail(processId, createReqVOList);
        return success(true);
    }

    @Operation(summary = "一键填充：从标准工艺复制到生产计划")
    @PostMapping("/copy-to-plan")
    public CommonResult<Boolean> copyProcessToPlan(
            @Parameter(name = "processId", description = "工艺ID", required = true) @RequestParam Long processId,
            @Parameter(name = "planId", description = "计划ID", required = true) @RequestParam Long planId) {
        productProcessService.copyProcessToPlan(processId, planId);
        return success(true);
    }
}
