package io.higgus.lab.module.bulong.controller.admin.production.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 生产记录分页查询 reqVO")
@Data
public class ProductionRecordPageReqVO {

    @Schema(description = "产品编号", example = "P001")
    private String productCode;

    @Schema(description = "是否有效", example = "1")
    private Boolean isActive;

    @Schema(description = "页码", example = "1")
    private Integer pageNo = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
