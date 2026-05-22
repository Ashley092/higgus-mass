package io.higgus.lab.module.bulong.controller.admin.process.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 管理后台 - 产品工艺主表更新 reqVO
 */
@Schema(description = "管理后台 - 产品工艺主表更新 reqVO")
@Data
public class ProductProcessUpdateReqVO {

    @Schema(description = "工艺ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "工艺ID不能为空")
    private Long id;

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

    @Schema(description = "是否启用", example = "true")
    private Boolean isActive;

    @Schema(description = "备注", example = "标准工艺参数")
    private String remark;
}
