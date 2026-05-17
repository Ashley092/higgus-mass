package io.higgus.lab.module.bulong.controller.admin.production.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "管理后台 - 梳栉工艺明细创建 reqVO")
@Data
public class GbDetailCreateReqVO {

    @Schema(description = "关联的生产记录序号", example = "PR001")
    @Size(max = 32, message = "生产记录序号长度不能超过32个字符")
    private String recordId;

    @Schema(description = "梳栉序号 (1=GB1, 2=GB2...)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "梳栉序号不能为空")
    @Min(value = 1, message = "梳栉序号最小为1")
    @Max(value = 99, message = "梳栉序号最大为99")
    private Integer gbIndex;

    @Schema(description = "送经量", example = "1200")
    private Integer yarnFeed;

    @Schema(description = "头纹/花型类型", example = "A型")
    @Size(max = 255, message = "头纹类型长度不能超过255个字符")
    private String patternType;

    @Schema(description = "穿丝方式", example = "满穿")
    @Size(max = 100, message = "穿丝方式长度不能超过100个字符")
    private String threadingType;

    @Schema(description = "原料规格", example = "75D/72F")
    @Size(max = 255, message = "原料规格长度不能超过255个字符")
    private String materialSpec;

    @Schema(description = "花高", example = "25.5")
    @Size(max = 255, message = "花高长度不能超过255个字符")
    private String patternHeight;

    @Schema(description = "用纱量", example = "12.5kg")
    @Size(max = 100, message = "用纱量长度不能超过100个字符")
    private String yarnUsage;
}
