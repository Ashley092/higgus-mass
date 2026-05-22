package io.higgus.lab.module.bulong.dal.dataobject.plan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.higgus.lab.framework.mybatis.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 生产计划表 DO
 */
@EqualsAndHashCode(callSuper = true)
@TableName("bl_production_plan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionPlanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 计划编码
     */
    private String planCode;

    /**
     * 关联产品ID
     */
    private Long productId;

    /**
     * 计划开始时间
     */
    private LocalDateTime plannedStart;

    /**
     * 计划结束时间
     */
    private LocalDateTime plannedEnd;

    /**
     * 计划产量
     */
    private BigDecimal plannedOutput;

    /**
     * 计划时长（分钟）
     */
    private Integer plannedDuration;

    /**
     * 实际开始时间
     */
    private LocalDateTime actualStart;

    /**
     * 实际结束时间
     */
    private LocalDateTime actualEnd;

    /**
     * 实际产量
     */
    private BigDecimal actualOutput;

    /**
     * 实际使用车速
     */
    private Integer speedSetting;

    /**
     * 分配机台ID
     */
    private Long machineId;

    /**
     * 质量目标（合格率%）
     */
    private BigDecimal qualityTarget;

    /**
     * 疪品数量
     */
    private BigDecimal defectOutput;

    /**
     * 疪品率
     */
    private BigDecimal defectRate;

    /**
     * 优先级 1-10，1最高
     */
    private Integer priority;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核人
     */
    private String approvedBy;

    /**
     * 审核时间
     */
    private LocalDateTime approvedAt;
}
