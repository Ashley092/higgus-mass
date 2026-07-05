package io.higgus.lab.module.storage.controller.collab.vo.collab.space;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "创建协作空间请求")
public class CollaborationSpaceCreateReqVO {

    @NotBlank(message = "空间名称不能为空")
    @Size(max = 64, message = "空间名称长度不能超过64")
    @Schema(description = "空间名称", example = "技术研发团队")
    private String name;

    @Size(max = 255, message = "空间描述长度不能超过255")
    @Schema(description = "空间描述", example = "负责产品研发的技术团队")
    private String description;
}
