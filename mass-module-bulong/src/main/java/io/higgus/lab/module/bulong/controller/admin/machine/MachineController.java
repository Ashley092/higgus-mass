package io.higgus.lab.module.bulong.controller.admin.machine;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.mass.framework.common.util.object.PageResult;
import io.higgus.lab.module.bulong.controller.admin.machine.vo.*;
import io.higgus.lab.module.bulong.service.machine.MachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "布隆后台 - 机台管理")
@RestController
@RequestMapping("/admin/bulong/machine")
@Validated
public class MachineController {

    @Resource
    private MachineService machineService;

    @Operation(summary = "创建机台")
    @PostMapping("/create")
    public CommonResult<Long> createMachine(@RequestBody @Valid MachineCreateReqVO createReqVO) {
        Long id = machineService.createMachine(createReqVO);
        return success(id);
    }

    @Operation(summary = "更新机台")
    @PutMapping("/update")
    public CommonResult<Boolean> updateMachine(@RequestBody @Valid MachineUpdateReqVO updateReqVO) {
        machineService.updateMachine(updateReqVO);
        return success(true);
    }

    @Operation(summary = "删除机台")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteMachine(@Parameter(name = "id", description = "机台ID", required = true) @RequestParam Long id) {
        machineService.deleteMachine(id);
        return success(true);
    }

    @Operation(summary = "获取机台（根据ID）")
    @GetMapping("/get")
    public CommonResult<MachineRespVO> getMachine(@Parameter(name = "id", description = "机台ID", required = true) @RequestParam Long id) {
        MachineRespVO machine = machineService.getMachine(id);
        return success(machine);
    }

    @Operation(summary = "获取机台（根据编码）")
    @GetMapping("/get-by-code")
    public CommonResult<MachineRespVO> getMachineByCode(@Parameter(name = "machineCode", description = "机台编码", required = true) @RequestParam String machineCode) {
        MachineRespVO machine = machineService.getMachineByCode(machineCode);
        return success(machine);
    }

    @Operation(summary = "获取机台列表")
    @GetMapping("/list")
    public CommonResult<List<MachineRespVO>> getMachineList(MachineListReqVO listReqVO) {
        List<MachineRespVO> list = machineService.getMachineList(listReqVO);
        return success(list);
    }

    @Operation(summary = "获取机台分页列表")
    @GetMapping("/page")
    public CommonResult<PageResult<MachineRespVO>> getMachinePage(MachinePageReqVO pageReqVO) {
        PageResult<MachineRespVO> result = machineService.getMachinePage(pageReqVO);
        return success(result);
    }

    @Operation(summary = "获取空闲机台列表")
    @GetMapping("/idle-list")
    public CommonResult<List<MachineRespVO>> getIdleMachineList() {
        List<MachineRespVO> list = machineService.getIdleMachineList();
        return success(list);
    }
}
