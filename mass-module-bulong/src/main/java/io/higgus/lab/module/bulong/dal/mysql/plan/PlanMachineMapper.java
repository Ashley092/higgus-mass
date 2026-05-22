package io.higgus.lab.module.bulong.dal.mysql.plan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.plan.PlanMachineDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 计划-机台关联表 Mapper
 */
@Mapper
public interface PlanMachineMapper extends BaseMapperX<PlanMachineDO> {

    /**
     * 根据计划ID查询关联列表
     */
    default List<PlanMachineDO> selectByPlanId(Long planId) {
        return selectList(new LambdaQueryWrapper<PlanMachineDO>()
                .eq(PlanMachineDO::getPlanId, planId));
    }

    /**
     * 根据机台ID查询关联列表
     */
    default List<PlanMachineDO> selectByMachineId(Long machineId) {
        return selectList(new LambdaQueryWrapper<PlanMachineDO>()
                .eq(PlanMachineDO::getMachineId, machineId));
    }

    /**
     * 检查计划-机台关联是否已存在
     */
    default PlanMachineDO selectByPlanAndMachine(Long planId, Long machineId) {
        return selectOne(new LambdaQueryWrapper<PlanMachineDO>()
                .eq(PlanMachineDO::getPlanId, planId)
                .eq(PlanMachineDO::getMachineId, machineId));
    }

    /**
     * 删除计划的所有关联
     */
    default void deleteByPlanId(Long planId) {
        delete(new LambdaQueryWrapper<PlanMachineDO>()
                .eq(PlanMachineDO::getPlanId, planId));
    }

    /**
     * 删除机台的所有关联
     */
    default void deleteByMachineId(Long machineId) {
        delete(new LambdaQueryWrapper<PlanMachineDO>()
                .eq(PlanMachineDO::getMachineId, machineId));
    }
}
