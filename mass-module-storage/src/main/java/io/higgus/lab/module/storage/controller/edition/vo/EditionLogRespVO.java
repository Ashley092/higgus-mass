package io.higgus.lab.module.storage.controller.edition.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编辑日志响应 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "编辑日志响应")
public class EditionLogRespVO {

    @Schema(description = "日志ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    @Schema(description = "内容ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long contentId;

    @Schema(description = "版本号", example = "1")
    private Integer version;

    @Schema(description = "行号（从0开始）", example = "0")
    private Integer rowIndex;

    @Schema(description = "列号（从0开始）", example = "0")
    private Integer colIndex;

    @Schema(description = "旧值")
    private String oldValue;

    @Schema(description = "新值")
    private String newValue;

    @Schema(description = "更新人ID", example = "10001")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updater;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private String createTime;
}