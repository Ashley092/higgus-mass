package io.higgus.lab.module.bulong.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - 计划-机台关联更新 reqVO
 */
@Schema(description = "管理后台 - 计划-机台关联更新 reqVO")
@Data
public class PlanMachineUpdateReqVO {

    @Schema(description = "关联ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "关联ID不能为空")
    private Long id;

    @Schema(description = "分配产量", example = "500.00")
    private BigDecimal allocatedOutput;

    @Schema(description = "时间窗口开始", example = "2026-05-20 08:00:00")
    private LocalDateTime timeWindowStart;

    @Schema(description = "时间窗口结束", example = "2026-05-20 12:00:00")
    private LocalDateTime timeWindowEnd;

    @Schema(description = "备注", example = "上午班次")
    private String remark;
}
