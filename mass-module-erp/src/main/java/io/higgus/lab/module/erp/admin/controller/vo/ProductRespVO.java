package io.higgus.lab.module.erp.admin.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 产品响应 VO
 *
 * @author system
 * @date 2025-12-16
 */
@Schema(description = "产品响应信息")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRespVO {

    @Schema(description = "产品编号", example = "P001")
    private Long id;

    @Schema(description = "产品代码", example = "P001")
    private String productCode;

    @Schema(description = "产品名称", example = "涤纶面料A")
    private String productName;

    @Schema(description = "理论门幅", example = "150.00")
    private Double tWidth;

    @Schema(description = "理论密度", example = "120.00")
    private Double tDensity;

    @Schema(description = "理论间隔", example = "2.50")
    private Double tInterval;

    @Schema(description = "记录日期", example = "2025-09-02 18:25:20")
    private LocalDateTime recordDate;

    @Schema(description = "是否有效（1：有效，0：无效）", example = "1")
    private Integer isValid;

}