package io.higgus.lab.module.bulong.controller.admin.production;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.bulong.controller.admin.production.vo.*;
import io.higgus.lab.module.bulong.service.production.MachineProductionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "布隆后台 - 机台采集数据管理")
@RestController
@RequestMapping("/admin/bulong/machine-production")
@Validated
public class MachineProductionController {

    @Resource
    private MachineProductionService machineProductionService;

    @Operation(summary = "创建采集数据")
    @PostMapping("/create")
    public CommonResult<Long> createMachineProduction(@RequestBody @Valid MachineProductionCreateReqVO createReqVO) {
        Long id = machineProductionService.createMachineProduction(createReqVO);
        return success(id);
    }

    @Operation(summary = "批量创建采集数据（用于工控系统推送）")
    @PostMapping("/batch-create")
    public CommonResult<Boolean> batchCreateMachineProduction(@RequestBody @Valid List<MachineProductionCreateReqVO> createReqVOList) {
        machineProductionService.batchCreateMachineProduction(createReqVOList);
        return success(true);
    }

    @Operation(summary = "根据机台ID和时间范围获取采集数据")
    @GetMapping("/list-by-machine-time")
    public CommonResult<List<MachineProductionRespVO>> getMachineProductionListByMachineAndTimeRange(
            @Parameter(name = "machineId", description = "机台ID", required = true) @RequestParam Long machineId,
            @Parameter(name = "startTime", description = "开始时间", required = true) @RequestParam LocalDateTime startTime,
            @Parameter(name = "endTime", description = "结束时间", required = true) @RequestParam LocalDateTime endTime) {
        List<MachineProductionRespVO> list = machineProductionService.getMachineProductionListByMachineAndTimeRange(
                machineId, startTime, endTime);
        return success(list);
    }

    @Operation(summary = "根据计划ID获取采集数据")
    @GetMapping("/list-by-plan")
    public CommonResult<List<MachineProductionRespVO>> getMachineProductionListByPlanId(
            @Parameter(name = "planId", description = "计划ID", required = true) @RequestParam Long planId) {
        List<MachineProductionRespVO> list = machineProductionService.getMachineProductionListByPlanId(planId);
        return success(list);
    }

    @Operation(summary = "获取机台最新采集数据")
    @GetMapping("/latest")
    public CommonResult<MachineProductionRespVO> getLatestMachineProduction(
            @Parameter(name = "machineId", description = "机台ID", required = true) @RequestParam Long machineId) {
        MachineProductionRespVO resp = machineProductionService.getLatestMachineProduction(machineId);
        return success(resp);
    }

    @Operation(summary = "获取机台在某时间点的采集数据")
    @GetMapping("/time-point")
    public CommonResult<MachineProductionRespVO> getMachineProductionByTimePoint(
            @Parameter(name = "machineId", description = "机台ID", required = true) @RequestParam Long machineId,
            @Parameter(name = "timePoint", description = "时间点", required = true) @RequestParam LocalDateTime timePoint) {
        MachineProductionRespVO resp = machineProductionService.getMachineProductionByTimePoint(machineId, timePoint);
        return success(resp);
    }
}
