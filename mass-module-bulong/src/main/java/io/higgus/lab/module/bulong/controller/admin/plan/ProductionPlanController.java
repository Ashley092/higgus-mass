package io.higgus.lab.module.bulong.controller.admin.plan;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;
import io.higgus.lab.module.bulong.service.plan.ProductionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "布隆后台 - 生产计划管理")
@RestController
@RequestMapping("/admin/bulong/plan")
@Validated
public class ProductionPlanController {

    @Resource
    private ProductionPlanService productionPlanService;

    @Operation(summary = "创建生产计划")
    @PostMapping("/create")
    public CommonResult<Long> createPlan(@RequestBody @Valid ProductionPlanCreateReqVO createReqVO) {
        Long id = productionPlanService.createPlan(createReqVO);
        return success(id);
    }

    @Operation(summary = "更新生产计划")
    @PutMapping("/update")
    public CommonResult<Boolean> updatePlan(@RequestBody @Valid ProductionPlanUpdateReqVO updateReqVO) {
        productionPlanService.updatePlan(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除生产计划")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deletePlan(@Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id) {
        productionPlanService.deletePlan(id);
        return success(true);
    }

    @Operation(summary = "获取生产计划（根据ID）")
    @GetMapping("/get")
    public CommonResult<ProductionPlanRespVO> getPlan(@Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id) {
        ProductionPlanRespVO plan = productionPlanService.getPlan(id);
        return success(plan);
    }

    @Operation(summary = "获取生产计划（根据编码）")
    @GetMapping("/get-by-code")
    public CommonResult<ProductionPlanRespVO> getPlanByCode(@Parameter(name = "planCode", description = "计划编码", required = true) @RequestParam String planCode) {
        ProductionPlanRespVO plan = productionPlanService.getPlanByCode(planCode);
        return success(plan);
    }

    @Operation(summary = "获取生产计划列表")
    @GetMapping("/list")
    public CommonResult<List<ProductionPlanRespVO>> getPlanList(ProductionPlanListReqVO listReqVO) {
        List<ProductionPlanRespVO> list = productionPlanService.getPlanList(listReqVO);
        return success(list);
    }

    @Operation(summary = "获取生产计划分页列表")
    @GetMapping("/page")
    public CommonResult<PageResult<ProductionPlanRespVO>> getPlanPage(ProductionPlanPageReqVO pageReqVO) {
        PageResult<ProductionPlanRespVO> result = productionPlanService.getPlanPage(pageReqVO);
        return success(result);
    }

    @Operation(summary = "获取未分配机台的计划列表")
    @GetMapping("/unassigned-list")
    public CommonResult<List<ProductionPlanRespVO>> getUnassignedPlanList() {
        List<ProductionPlanRespVO> list = productionPlanService.getUnassignedPlanList();
        return success(list);
    }

    @Operation(summary = "获取机台在指定时间范围内的计划")
    @GetMapping("/machine-time-range")
    public CommonResult<List<ProductionPlanRespVO>> getPlanListByMachineAndTimeRange(
            @Parameter(name = "machineId", description = "机台ID", required = true) @RequestParam Long machineId,
            @Parameter(name = "startTime", description = "开始时间", required = true) @RequestParam LocalDateTime startTime,
            @Parameter(name = "endTime", description = "结束时间", required = true) @RequestParam LocalDateTime endTime) {
        List<ProductionPlanRespVO> list = productionPlanService.getPlanListByMachineAndTimeRange(machineId, startTime, endTime);
        return success(list);
    }

    @Operation(summary = "提交计划审核")
    @PostMapping("/submit")
    public CommonResult<Boolean> submitForApproval(@Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id) {
        productionPlanService.submitForApproval(id);
        return success(true);
    }

    @Operation(summary = "审核通过")
    @PostMapping("/approve")
    public CommonResult<Boolean> approvePlan(
            @Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id,
            @Parameter(name = "approvedBy", description = "审核人", required = true) @RequestParam String approvedBy) {
        productionPlanService.approvePlan(id, approvedBy);
        return success(true);
    }

    @Operation(summary = "审核拒绝")
    @PostMapping("/reject")
    public CommonResult<Boolean> rejectPlan(@Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id) {
        productionPlanService.rejectPlan(id);
        return success(true);
    }

    @Operation(summary = "开始生产")
    @PostMapping("/start")
    public CommonResult<Boolean> startProduction(@Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id) {
        productionPlanService.startProduction(id);
        return success(true);
    }

    @Operation(summary = "完成生产")
    @PostMapping("/complete")
    public CommonResult<Boolean> completeProduction(
            @Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id,
            @Parameter(name = "actualOutput", description = "实际产量") @RequestParam(required = false) BigDecimal actualOutput,
            @Parameter(name = "defectOutput", description = "疪品数量") @RequestParam(required = false) BigDecimal defectOutput) {
        productionPlanService.completeProduction(id, actualOutput, defectOutput);
        return success(true);
    }

    @Operation(summary = "取消计划")
    @PostMapping("/cancel")
    public CommonResult<Boolean> cancelPlan(@Parameter(name = "id", description = "计划ID", required = true) @RequestParam Long id) {
        productionPlanService.cancelPlan(id);
        return success(true);
    }
}
