package io.higgus.lab.module.bulong.controller.admin.process.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - 产品工艺明细表响应 VO
 */
@Schema(description = "管理后台 - 产品工艺明细表响应 VO")
@Data
public class ProductProcessDetailRespVO {

    @Schema(description = "明细ID", example = "1")
    private Long id;

    @Schema(description = "关联工艺ID", example = "1")
    private Long processId;

    @Schema(description = "梳栉编号", example = "1")
    private Integer gbIndex;

    @Schema(description = "送纱量", example = "100.5")
    private BigDecimal yarnFeed;

    @Schema(description = "穿纱方式", example = "满穿")
    private String threadingType;

    @Schema(description = "纱线规格", example = "75D/72F")
    private String yarnCount;

    @Schema(description = "纱线品牌/型号", example = "某某品牌")
    private String yarnBrand;

    @Schema(description = "目标张力（N）", example = "10.5")
    private BigDecimal tensionTarget;

    @Schema(description = "针型", example = "复合针")
    private String needleType;

    @Schema(description = "机号/针距", example = "E28")
    private String needleGauge;

    @Schema(description = "梳栉排列图", example = "1-1-1-1")
    private String guideBarPattern;

    @Schema(description = "链节顺序", example = "1-2-3-4")
    private String chainLinkSequence;

    @Schema(description = "花型循环数", example = "4")
    private Integer patternRepeat;

    @Schema(description = "备注", example = "标准梳栉配置")
    private String remark;

    @Schema(description = "创建时间", example = "2026-05-20 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-05-20 10:00:00")
    private LocalDateTime updateTime;
}
