package io.higgus.lab.module.bulong.service.production;

import io.higgus.lab.module.bulong.controller.admin.production.vo.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机台采集数据 Service 接口
 */
public interface MachineProductionService {

    /**
     * 创建采集数据
     */
    Long createMachineProduction(MachineProductionCreateReqVO createReqVO);

    /**
     * 根据机台ID和时间范围获取采集数据
     */
    List<MachineProductionRespVO> getMachineProductionListByMachineAndTimeRange(
            Long machineId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据计划ID获取采集数据
     */
    List<MachineProductionRespVO> getMachineProductionListByPlanId(Long planId);

    /**
     * 获取机台最新采集数据
     */
    MachineProductionRespVO getLatestMachineProduction(Long machineId);

    /**
     * 获取机台在某时间点的采集数据
     */
    MachineProductionRespVO getMachineProductionByTimePoint(Long machineId, LocalDateTime timePoint);

    /**
     * 批量创建采集数据（用于工控系统推送）
     */
    void batchCreateMachineProduction(List<MachineProductionCreateReqVO> createReqVOList);
}
