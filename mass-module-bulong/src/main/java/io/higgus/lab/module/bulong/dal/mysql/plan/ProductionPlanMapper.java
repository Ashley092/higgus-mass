package io.higgus.lab.module.bulong.dal.mysql.plan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.bulong.dal.dataobject.plan.ProductionPlanDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产计划表 Mapper
 */
@Mapper
public interface ProductionPlanMapper extends BaseMapperX<ProductionPlanDO> {

    /**
     * 根据计划编码查询
     */
    default ProductionPlanDO selectByPlanCode(String planCode) {
        return selectOne(new LambdaQueryWrapper<ProductionPlanDO>()
                .eq(ProductionPlanDO::getPlanCode, planCode));
    }

    /**
     * 根据产品ID查询计划列表
     */
    default List<ProductionPlanDO> selectByProductId(Long productId) {
        return selectList(new LambdaQueryWrapper<ProductionPlanDO>()
                .eq(ProductionPlanDO::getProductId, productId)
                .orderByDesc(ProductionPlanDO::getCreateTime));
    }

    /**
     * 根据机台ID查询计划列表
     */
    default List<ProductionPlanDO> selectByMachineId(Long machineId) {
        return selectList(new LambdaQueryWrapper<ProductionPlanDO>()
                .eq(ProductionPlanDO::getMachineId, machineId)
                .orderByDesc(ProductionPlanDO::getPlannedStart));
    }

    /**
     * 根据状态查询计划列表
     */
    default List<ProductionPlanDO> selectByStatus(String status) {
        return selectList(new LambdaQueryWrapper<ProductionPlanDO>()
                .eq(ProductionPlanDO::getStatus, status)
                .orderByAsc(ProductionPlanDO::getPriority, ProductionPlanDO::getPlannedStart));
    }

    /**
     * 查询未分配机台的计划
     */
    default List<ProductionPlanDO> selectUnassignedPlans() {
        return selectList(new LambdaQueryWrapper<ProductionPlanDO>()
                .isNull(ProductionPlanDO::getMachineId)
                .eq(ProductionPlanDO::getStatus, "APPROVED")
                .orderByAsc(ProductionPlanDO::getPriority, ProductionPlanDO::getPlannedStart));
    }

    /**
     * 根据时间范围查询计划
     */
    default List<ProductionPlanDO> selectByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapper<ProductionPlanDO>()
                .lt(ProductionPlanDO::getPlannedStart, endTime)
                .gt(ProductionPlanDO::getPlannedEnd, startTime)
                .orderByAsc(ProductionPlanDO::getPlannedStart));
    }

    /**
     * 查询某机台在某时间范围内的计划
     */
    default List<ProductionPlanDO> selectByMachineAndTimeRange(
            @Param("machineId") Long machineId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapper<ProductionPlanDO>()
                .eq(ProductionPlanDO::getMachineId, machineId)
                .lt(ProductionPlanDO::getPlannedStart, endTime)
                .gt(ProductionPlanDO::getPlannedEnd, startTime)
                .orderByAsc(ProductionPlanDO::getPlannedStart));
    }

}
