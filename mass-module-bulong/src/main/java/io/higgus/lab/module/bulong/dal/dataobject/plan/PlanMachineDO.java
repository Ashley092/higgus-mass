package io.higgus.lab.module.bulong.dal.dataobject.plan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 计划-机台关联表 DO
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_plan_machine")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanMachineDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 关联计划ID
     */
    private Long planId;

    /**
     * 关联机台ID
     */
    private Long machineId;

    /**
     * 分配产量
     */
    private BigDecimal allocatedOutput;

    /**
     * 时间窗口开始
     */
    private LocalDateTime timeWindowStart;

    /**
     * 时间窗口结束
     */
    private LocalDateTime timeWindowEnd;

    /**
     * 分配人
     */
    private String assignedBy;

    /**
     * 分配时间
     */
    private LocalDateTime assignedAt;

    /**
     * 备注
     */
    private String remark;
}
