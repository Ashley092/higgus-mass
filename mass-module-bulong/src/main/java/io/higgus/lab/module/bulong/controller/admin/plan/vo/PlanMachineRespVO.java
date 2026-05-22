package io.higgus.lab.module.bulong.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - 计划-机台关联响应 VO
 */
@Schema(description = "管理后台 - 计划-机台关联响应 VO")
@Data
public class PlanMachineRespVO {

    @Schema(description = "关联ID", example = "1")
    private Long id;

    @Schema(description = "关联计划ID", example = "1")
    private Long planId;

    @Schema(description = "计划编码（扩展字段）")
    private String planCode;

    @Schema(description = "关联机台ID", example = "1")
    private Long machineId;

    @Schema(description = "机台编码（扩展字段）")
    private String machineCode;

    @Schema(description = "机台名称（扩展字段）")
    private String machineName;

    @Schema(description = "分配产量", example = "500.00")
    private BigDecimal allocatedOutput;

    @Schema(description = "时间窗口开始", example = "2026-05-20 08:00:00")
    private LocalDateTime timeWindowStart;

    @Schema(description = "时间窗口结束", example = "2026-05-20 12:00:00")
    private LocalDateTime timeWindowEnd;

    @Schema(description = "分配人", example = "admin")
    private String assignedBy;

    @Schema(description = "分配时间", example = "2026-05-20 07:00:00")
    private LocalDateTime assignedAt;

    @Schema(description = "备注", example = "上午班次")
    private String remark;

    @Schema(description = "创建时间", example = "2026-05-20 07:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-05-20 07:00:00")
    private LocalDateTime updateTime;
}
