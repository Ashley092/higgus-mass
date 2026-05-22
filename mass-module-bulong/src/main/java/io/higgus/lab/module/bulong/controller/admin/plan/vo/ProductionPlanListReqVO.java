package io.higgus.lab.module.bulong.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理后台 - 生产计划查询 reqVO
 */
@Schema(description = "管理后台 - 生产计划查询 reqVO")
@Data
public class ProductionPlanListReqVO {

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
}
