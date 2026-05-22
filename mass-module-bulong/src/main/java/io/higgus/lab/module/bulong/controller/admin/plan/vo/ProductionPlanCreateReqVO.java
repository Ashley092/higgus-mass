package io.higgus.lab.module.bulong.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - 生产计划创建 reqVO
 */
@Schema(description = "管理后台 - 生产计划创建 reqVO")
@Data
public class ProductionPlanCreateReqVO {

    @Schema(description = "计划编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "PP20260520001")
    @NotBlank(message = "计划编码不能为空")
    @Size(max = 50, message = "计划编码长度不能超过50个字符")
    private String planCode;

    @Schema(description = "关联产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long productId;

    @Schema(description = "计划开始时间", example = "2026-05-20 08:00:00")
    private LocalDateTime plannedStart;

    @Schema(description = "计划结束时间", example = "2026-05-20 16:00:00")
    private LocalDateTime plannedEnd;

    @Schema(description = "计划产量", example = "1000.00")
    private BigDecimal plannedOutput;

    @Schema(description = "计划时长（分钟）", example = "480")
    private Integer plannedDuration;

    @Schema(description = "实际使用车速", example = "2000")
    private Integer speedSetting;

    @Schema(description = "分配机台ID", example = "1")
    private Long machineId;

    @Schema(description = "质量目标（合格率%）", example = "99.5")
    private BigDecimal qualityTarget;

    @Schema(description = "优先级 1-10，1最高", example = "5")
    private Integer priority;

    @Schema(description = "状态", example = "DRAFT")
    private String status;

    @Schema(description = "备注", example = "紧急订单")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
