package io.higgus.lab.module.bulong.controller.admin.production.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 生产记录响应 VO")
@Data
public class ProductionRecordRespVO {

    @Schema(description = "生产记录序号", example = "PR001")
    private String recordId;

    @Schema(description = "产品编号", example = "P001")
    private String productCode;

    @Schema(description = "实际门幅", example = "148.50")
    private BigDecimal actualWidth;

    @Schema(description = "实际密度", example = "119.00")
    private BigDecimal actualDensity;

    @Schema(description = "实际间隔", example = "2.48")
    private BigDecimal actualSpacing;

    @Schema(description = "克重 (g/㎡)", example = "85.50")
    private BigDecimal weightGsm;

    @Schema(description = "生产机台号", example = "M001")
    private String machineCode;

    @Schema(description = "载入时间", example = "2025-09-02 18:25:20")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", example = "2025-09-02 18:25:20")
    private LocalDateTime updatedAt;

    @Schema(description = "是否有效 (1: 有效, 0: 无效)", example = "1")
    private Boolean isActive;

    @Schema(description = "备注", example = "正常生产")
    private String remark;

    @Schema(description = "梳栉工艺明细列表")
    private List<GbDetailRespVO> gbDetails;
}
