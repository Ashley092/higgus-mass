package io.higgus.lab.module.bulong.controller.admin.production.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 梳栉工艺明细响应 VO")
@Data
public class GbDetailRespVO {

    @Schema(description = "明细自增ID", example = "1")
    private Long detailId;

    @Schema(description = "关联的生产记录序号", example = "PR001")
    private String recordId;

    @Schema(description = "梳栉序号 (1=GB1, 2=GB2...)", example = "1")
    private Integer gbIndex;

    @Schema(description = "送经量", example = "1200")
    private Integer yarnFeed;

    @Schema(description = "头纹/花型类型", example = "A型")
    private String patternType;

    @Schema(description = "穿丝方式", example = "满穿")
    private String threadingType;

    @Schema(description = "原料规格", example = "75D/72F")
    private String materialSpec;

    @Schema(description = "花高", example = "25.5")
    private String patternHeight;

    @Schema(description = "用纱量", example = "12.5kg")
    private String yarnUsage;
}
