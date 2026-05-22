package io.higgus.lab.module.bulong.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - 生产计划更新 reqVO
 */
@Schema(description = "管理后台 - 生产计划更新 reqVO")
@Data
public class ProductionPlanUpdateReqVO {

    @Schema(description = "计划ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "计划ID不能为空")
    private Long id;

    @Schema(description = "计划编码", example = "PP20260520001")
    private String planCode;

    @Schema(description = "关联产品ID", example = "1")
    private Long productId;

    @Schema(description = "计划开始时间", example = "2026-05-20 08:00:00")
    private LocalDateTime plannedStart;

    @Schema(description = "计划结束时间", example = "2026-05-20 16:00:00")
    private LocalDateTime plannedEnd;

    @Schema(description = "计划产量", example = "1000.00")
    private BigDecimal plannedOutput;

    @Schema(description = "计划时长（分钟）", example = "480")
    private Integer plannedDuration;

    @Schema(description = "实际开始时间", example = "2026-05-20 08:05:00")
    private LocalDateTime actualStart;

    @Schema(description = "实际结束时间", example = "2026-05-20 16:10:00")
    private LocalDateTime actualEnd;

    @Schema(description = "实际产量", example = "1050.00")
    private BigDecimal actualOutput;

    @Schema(description = "实际使用车速", example = "2000")
    private Integer speedSetting;

    @Schema(description = "分配机台ID", example = "1")
    private Long machineId;

    @Schema(description = "质量目标（合格率%）", example = "99.5")
    private BigDecimal qualityTarget;

    @Schema(description = "疪品数量", example = "5.00")
    private BigDecimal defectOutput;

    @Schema(description = "疪品率", example = "0.48")
    private BigDecimal defectRate;

    @Schema(description = "优先级 1-10，1最高", example = "3")
    private Integer priority;

    @Schema(description = "状态", example = "COMPLETED")
    private String status;

    @Schema(description = "审核人", example = "admin")
    private String approvedBy;

    @Schema(description = "审核时间", example = "2026-05-20 07:00:00")
    private LocalDateTime approvedAt;

    @Schema(description = "备注", example = "紧急订单")
    private String remark;
}
