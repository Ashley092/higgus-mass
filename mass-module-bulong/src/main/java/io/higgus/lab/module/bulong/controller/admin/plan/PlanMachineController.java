package io.higgus.lab.module.bulong.controller.admin.plan;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;
import io.higgus.lab.module.bulong.service.plan.PlanMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "布隆后台 - 计划-机台关联管理")
@RestController
@RequestMapping("/admin/bulong/plan-machine")
@Validated
public class PlanMachineController {

    @Resource
    private PlanMachineService planMachineService;

    @Operation(summary = "创建计划-机台关联")
    @PostMapping("/create")
    public CommonResult<Long> createPlanMachine(@RequestBody @Valid PlanMachineCreateReqVO createReqVO) {
        Long id = planMachineService.createPlanMachine(createReqVO);
        return success(id);
    }

    @Operation(summary = "更新计划-机台关联")
    @PutMapping("/update")
    public CommonResult<Boolean> updatePlanMachine(@RequestBody @Valid PlanMachineUpdateReqVO updateReqVO) {
        planMachineService.updatePlanMachine(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除计划-机台关联")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deletePlanMachine(@Parameter(name = "id", description = "关联ID", required = true) @RequestParam Long id) {
        planMachineService.deletePlanMachine(id);
        return success(true);
    }

    @Operation(summary = "获取计划-机台关联")
    @GetMapping("/get")
    public CommonResult<PlanMachineRespVO> getPlanMachine(@Parameter(name = "id", description = "关联ID", required = true) @RequestParam Long id) {
        PlanMachineRespVO resp = planMachineService.getPlanMachine(id);
        return success(resp);
    }

    @Operation(summary = "根据计划ID获取关联列表")
    @GetMapping("/list-by-plan")
    public CommonResult<List<PlanMachineRespVO>> getPlanMachineListByPlanId(
            @Parameter(name = "planId", description = "计划ID", required = true) @RequestParam Long planId) {
        List<PlanMachineRespVO> list = planMachineService.getPlanMachineListByPlanId(planId);
        return success(list);
    }

    @Operation(summary = "根据机台ID获取关联列表")
    @GetMapping("/list-by-machine")
    public CommonResult<List<PlanMachineRespVO>> getPlanMachineListByMachineId(
            @Parameter(name = "machineId", description = "机台ID", required = true) @RequestParam Long machineId) {
        List<PlanMachineRespVO> list = planMachineService.getPlanMachineListByMachineId(machineId);
        return success(list);
    }

    @Operation(summary = "批量创建计划-机台关联")
    @PostMapping("/batch-create")
    public CommonResult<Boolean> batchCreatePlanMachine(
            @Parameter(name = "planId", description = "计划ID", required = true) @RequestParam Long planId,
            @Parameter(name = "machineIds", description = "机台ID列表", required = true) @RequestBody List<Long> machineIds) {
        planMachineService.batchCreatePlanMachine(planId, machineIds);
        return success(true);
    }

    @Operation(summary = "批量删除计划-机台关联")
    @PostMapping("/batch-delete")
    public CommonResult<Boolean> batchDeletePlanMachine(
            @Parameter(name = "planId", description = "计划ID", required = true) @RequestParam Long planId) {
        planMachineService.batchDeletePlanMachine(planId);
        return success(true);
    }
}
