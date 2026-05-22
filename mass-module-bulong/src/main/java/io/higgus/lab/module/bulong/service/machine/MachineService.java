package io.higgus.lab.module.bulong.service.machine;

import io.higgus.lab.module.bulong.controller.admin.machine.vo.*;
import io.higgus.lab.mass.framework.common.util.object.PageResult;

import java.util.List;

/**
 * 机台管理 Service 接口
 */
public interface MachineService {

    /**
     * 创建机台
     */
    Long createMachine(MachineCreateReqVO createReqVO);

    /**
     * 更新机台
     */
    void updateMachine(MachineUpdateReqVO updateReqVO);

    /**
     * 删除机台
     */
    void deleteMachine(Long id);

    /**
     * 根据ID获取机台
     */
    MachineRespVO getMachine(Long id);

    /**
     * 根据机台编码获取机台
     */
    MachineRespVO getMachineByCode(String machineCode);

    /**
     * 获取机台列表
     */
    List<MachineRespVO> getMachineList(MachineListReqVO listReqVO);

    /**
     * 获取机台分页
     */
    PageResult<MachineRespVO> getMachinePage(MachinePageReqVO pageReqVO);

    /**
     * 获取空闲机台列表
     */
    List<MachineRespVO> getIdleMachineList();
}
