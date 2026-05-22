package io.higgus.lab.module.bulong.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理后台 - 生产计划分页查询 reqVO
 */
@Schema(description = "管理后台 - 生产计划分页查询 reqVO")
@Data
public class ProductionPlanPageReqVO {

    @Schema(description = "页码", example = "1")
    private Integer pageNo = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "计划编码", example = "PP20260520001")
    private String planCode;

    @Schema(description = "关联产品ID", example = "1")
    private Long productId;

    @Schema(description = "分配机台ID", example = "1")
    private Long machineId;

    @Schema(description = "状态", example = "APPROVED")
    private String status;

    @Schema(description = "优先级", example = "5")
    private Integer priority;

    @Schema(description = "计划开始时间起", example = "2026-05-01 00:00:00")
    private LocalDateTime plannedStartFrom;

    @Schema(description = "计划开始时间止", example = "2026-05-31 23:59:59")
    private LocalDateTime plannedStartTo;
}
