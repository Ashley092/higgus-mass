package io.higgus.lab.module.storage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "更新协作空间请求")
public class CollaborationSpaceUpdateReqVO {

    @NotNull(message = "空间ID不能为空")
    @Schema(description = "空间ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Size(max = 64, message = "空间名称长度不能超过64")
    @Schema(description = "空间名称", example = "技术研发团队")
    private String name;

    @Size(max = 255, message = "空间描述长度不能超过255")
    @Schema(description = "空间描述", example = "负责产品研发的技术团队")
    private String description;

    @Schema(description = "状态: 0正常, 1禁用/归档", example = "0")
    private Integer status;
}
