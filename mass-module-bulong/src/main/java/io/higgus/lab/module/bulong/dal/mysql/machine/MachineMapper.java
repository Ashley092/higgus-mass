package io.higgus.lab.module.bulong.dal.mysql.machine;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.machine.MachineDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 机台表 Mapper
 */
@Mapper
public interface MachineMapper extends BaseMapperX<MachineDO> {

    /**
     * 根据机台编码查询
     */
    default MachineDO selectByMachineCode(String machineCode) {
        return selectOne(new LambdaQueryWrapper<MachineDO>()
                .eq(MachineDO::getMachineCode, machineCode));
    }

    /**
     * 根据状态查询机台列表
     */
    default List<MachineDO> selectByStatus(String status) {
        return selectList(new LambdaQueryWrapper<MachineDO>()
                .eq(MachineDO::getStatus, status));
    }

    /**
     * 根据机台类型查询机台列表
     */
    default List<MachineDO> selectByMachineType(String machineType) {
        return selectList(new LambdaQueryWrapper<MachineDO>()
                .eq(MachineDO::getMachineType, machineType));
    }

    /**
     * 根据车间查询机台列表
     */
    default List<MachineDO> selectByWorkshop(String workshop) {
        return selectList(new LambdaQueryWrapper<MachineDO>()
                .eq(MachineDO::getWorkshop, workshop));
    }

    /**
     * 查询空闲机台列表
     */
    default List<MachineDO> selectIdleMachines() {
        return selectList(new LambdaQueryWrapper<MachineDO>()
                .eq(MachineDO::getStatus, "IDLE"));
    }

    /**
     * 批量查询机台
     */
    default List<MachineDO> selectByIds(@Param("ids") List<Long> ids) {
        return selectList(new LambdaQueryWrapper<MachineDO>()
                .in(MachineDO::getId, ids));
    }
}
