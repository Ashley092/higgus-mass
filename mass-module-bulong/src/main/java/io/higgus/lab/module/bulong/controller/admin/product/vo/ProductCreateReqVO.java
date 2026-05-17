package io.higgus.lab.module.bulong.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产品创建 reqVO")
@Data
public class ProductCreateReqVO {

    @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "P001")
    @NotBlank(message = "产品编号不能为空")
    @Size(max = 20, message = "产品编号长度不能超过20个字符")
    private String productCode;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "涤纶面料A")
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 50, message = "产品名称长度不能超过50个字符")
    private String productName;

    @Schema(description = "理论门幅", example = "150.00")
    private BigDecimal theoreticalWidth;

    @Schema(description = "理论密度", example = "120.00")
    private BigDecimal theoreticalDensity;

    @Schema(description = "理论间隔", example = "2.50")
    private BigDecimal theoreticalSpacing;

    @Schema(description = "记录日期", example = "2025-09-02 18:25:20")
    private LocalDateTime recordDate;

    @Schema(description = "是否有效 (1: 有效, 0: 无效)", example = "1")
    private Boolean isActive;

    @Schema(description = "产品路由ID", example = "P20250902182520533943001")
    private String productRouteId;
}
