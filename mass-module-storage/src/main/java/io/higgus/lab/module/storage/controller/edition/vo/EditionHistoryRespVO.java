package io.higgus.lab.module.storage.controller.edition.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 编辑历史响应 VO（用于返回版本列表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "编辑历史响应")
public class EditionHistoryRespVO {

    @Schema(description = "内容ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long contentId;

    @Schema(description = "当前版本号", example = "10")
    private Integer currentVersion;

    @Schema(description = "历史记录总数", example = "10")
    private Long totalCount;

    @Schema(description = "历史记录列表")
    private List<EditionLogRespVO> logs;
}
