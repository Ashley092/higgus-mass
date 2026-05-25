package io.higgus.lab.module.storage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "项目/渠道响应")
public class CollaborationItemRespVO {

    @Schema(description = "项目/渠道ID", example = "1")
    private Long id;

    @Schema(description = "所属空间ID", example = "1")
    private Long spaceId;

    @Schema(description = "项目/渠道/知识库名称", example = "后端项目A")
    private String name;

    @Schema(description = "类型: 1项目(Project), 2渠道(Channel), 3知识库(Wiki)", example = "1")
    private Integer type;

    @Schema(description = "创建者", example = "admin")
    private String creator;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private String createTime;

    @Schema(description = "更新者", example = "admin")
    private String updater;

    @Schema(description = "更新时间", example = "2024-01-01 10:00:00")
    private String updateTime;
}
