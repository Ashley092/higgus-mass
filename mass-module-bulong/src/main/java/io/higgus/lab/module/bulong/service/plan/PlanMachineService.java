package io.higgus.lab.module.bulong.service.plan;

import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;

import java.util.List;

/**
 * 计划-机台关联管理 Service 接口
 */
public interface PlanMachineService {

    /**
     * 创建计划-机台关联
     */
    Long createPlanMachine(PlanMachineCreateReqVO createReqVO);

    /**
     * 更新计划-机台关联
     */
    void updatePlanMachine(PlanMachineUpdateReqVO updateReqVO);

    /**
     * 删除计划-机台关联
     */
    void deletePlanMachine(Long id);

    /**
     * 根据ID获取计划-机台关联
     */
    PlanMachineRespVO getPlanMachine(Long id);

    /**
     * 根据计划ID获取关联列表
     */
    List<PlanMachineRespVO> getPlanMachineListByPlanId(Long planId);

    /**
     * 根据机台ID获取关联列表
     */
    List<PlanMachineRespVO> getPlanMachineListByMachineId(Long machineId);

    /**
     * 批量创建计划-机台关联
     */
    void batchCreatePlanMachine(Long planId, List<Long> machineIds);

    /**
     * 批量删除计划-机台关联
     */
    void batchDeletePlanMachine(Long planId);
}
