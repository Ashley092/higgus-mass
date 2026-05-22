package io.higgus.lab.module.bulong.service.plan;

import io.higgus.lab.module.bulong.controller.admin.plan.vo.*;
import io.higgus.lab.mass.framework.common.util.object.PageResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产计划管理 Service 接口
 */
public interface ProductionPlanService {

    /**
     * 创建生产计划
     */
    Long createPlan(ProductionPlanCreateReqVO createReqVO);

    /**
     * 更新生产计划
     */
    void updatePlan(ProductionPlanUpdateReqVO updateReqVO);

    /**
     * 删除生产计划
     */
    void deletePlan(Long id);

    /**
     * 根据ID获取生产计划
     */
    ProductionPlanRespVO getPlan(Long id);

    /**
     * 根据计划编码获取生产计划
     */
    ProductionPlanRespVO getPlanByCode(String planCode);

    /**
     * 获取生产计划列表
     */
    List<ProductionPlanRespVO> getPlanList(ProductionPlanListReqVO listReqVO);

    /**
     * 获取生产计划分页
     */
    PageResult<ProductionPlanRespVO> getPlanPage(ProductionPlanPageReqVO pageReqVO);

    /**
     * 获取未分配机台的计划列表
     */
    List<ProductionPlanRespVO> getUnassignedPlanList();

    /**
     * 获取某机台在指定时间范围内的计划
     */
    List<ProductionPlanRespVO> getPlanListByMachineAndTimeRange(Long machineId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 提交计划审核
     */
    void submitForApproval(Long id);

    /**
     * 审核通过
     */
    void approvePlan(Long id, String approvedBy);

    /**
     * 审核拒绝
     */
    void rejectPlan(Long id);

    /**
     * 开始生产
     */
    void startProduction(Long id);

    /**
     * 完成生产
     */
    void completeProduction(Long id, BigDecimal actualOutput, BigDecimal defectOutput);

    /**
     * 取消计划
     */
    void cancelPlan(Long id);
}
