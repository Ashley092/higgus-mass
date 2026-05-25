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
@Schema(description = "协作空间响应")
public class CollaborationSpaceRespVO {

    @Schema(description = "空间ID", example = "1")
    private Long id;

    @Schema(description = "空间名称", example = "技术研发团队")
    private String name;

    @Schema(description = "空间描述", example = "负责产品研发的技术团队")
    private String description;

    @Schema(description = "状态: 0正常, 1禁用/归档", example = "0")
    private Integer status;

    @Schema(description = "创建者", example = "admin")
    private String creator;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private String createTime;

    @Schema(description = "更新者", example = "admin")
    private String updater;

    @Schema(description = "更新时间", example = "2024-01-01 10:00:00")
    private String updateTime;
}
