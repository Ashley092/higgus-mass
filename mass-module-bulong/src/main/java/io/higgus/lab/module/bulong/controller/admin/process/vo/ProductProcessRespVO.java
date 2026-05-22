package io.higgus.lab.module.bulong.controller.admin.process.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - 产品工艺主表响应 VO
 */
@Schema(description = "管理后台 - 产品工艺主表响应 VO")
@Data
public class ProductProcessRespVO {

    @Schema(description = "工艺ID", example = "1")
    private Long id;

    @Schema(description = "关联产品ID", example = "1")
    private Long productId;

    @Schema(description = "产品名称（扩展字段）")
    private String productName;

    @Schema(description = "适用机台类型", example = "高速特里科")
    private String machineType;

    @Schema(description = "工艺编号", example = "PROC001")
    private String processCode;

    @Schema(description = "工艺名称", example = "标准工艺A")
    private String processName;

    @Schema(description = "推荐车速（转/分）", example = "2000")
    private Integer speedSetting;

    @Schema(description = "温度设置（℃）", example = "25.0")
    private BigDecimal temperatureSetting;

    @Schema(description = "湿度设置（%）", example = "60.0")
    private BigDecimal humiditySetting;

    @Schema(description = "张力设置（N）", example = "10.5")
    private BigDecimal tensionSetting;

    @Schema(description = "辊筒速度（m/min）", example = "15.5")
    private BigDecimal rollerSpeed;

    @Schema(description = "版本号", example = "1")
    private Integer version;

    @Schema(description = "是否启用", example = "true")
    private Boolean isActive;

    @Schema(description = "备注", example = "标准工艺参数")
    private String remark;

    @Schema(description = "创建时间", example = "2026-05-20 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-05-20 10:00:00")
    private LocalDateTime updateTime;
}
