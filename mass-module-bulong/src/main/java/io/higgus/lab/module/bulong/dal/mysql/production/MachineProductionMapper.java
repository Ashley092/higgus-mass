package io.higgus.lab.module.bulong.dal.mysql.production;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.production.MachineProductionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机台实际生产表 Mapper
 */
@Mapper
public interface MachineProductionMapper extends BaseMapperX<MachineProductionDO> {

    /**
     * 根据机台ID和时间范围查询采集数据
     */
    default List<MachineProductionDO> selectByMachineAndTimeRange(
            @Param("machineId") Long machineId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapper<MachineProductionDO>()
                .eq(MachineProductionDO::getMachineId, machineId)
                .between(MachineProductionDO::getCollectTime, startTime, endTime)
                .orderByAsc(MachineProductionDO::getCollectTime));
    }

    /**
     * 根据计划ID查询采集数据
     */
    default List<MachineProductionDO> selectByPlanId(Long planId) {
        return selectList(new LambdaQueryWrapper<MachineProductionDO>()
                .eq(MachineProductionDO::getPlanId, planId)
                .orderByAsc(MachineProductionDO::getCollectTime));
    }

    /**
     * 查询机台最新采集数据
     */
    default MachineProductionDO selectLatestByMachineId(Long machineId) {
        return selectOne(new LambdaQueryWrapper<MachineProductionDO>()
                .eq(MachineProductionDO::getMachineId, machineId)
                .orderByDesc(MachineProductionDO::getCollectTime)
                .last("LIMIT 1"));
    }

    /**
     * 查询机台在某时间点的采集数据
     */
    default MachineProductionDO selectByMachineAndTimePoint(
            @Param("machineId") Long machineId,
            @Param("timePoint") LocalDateTime timePoint) {
        return selectOne(new LambdaQueryWrapper<MachineProductionDO>()
                .eq(MachineProductionDO::getMachineId, machineId)
                .le(MachineProductionDO::getCollectTime, timePoint)
                .orderByDesc(MachineProductionDO::getCollectTime)
                .last("LIMIT 1"));
    }

    /**
     * 根据班次查询采集数据
     */
    default List<MachineProductionDO> selectByMachineAndShift(
            @Param("machineId") Long machineId,
            @Param("shiftNo") String shiftNo) {
        return selectList(new LambdaQueryWrapper<MachineProductionDO>()
                .eq(MachineProductionDO::getMachineId, machineId)
                .eq(MachineProductionDO::getShiftNo, shiftNo)
                .orderByAsc(MachineProductionDO::getCollectTime));
    }
}
