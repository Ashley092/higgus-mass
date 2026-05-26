package io.higgus.lab.module.storage.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "创建项目/渠道请求")
public class CollaborationItemCreateReqVO {

    @NotNull(message = "空间ID不能为空")
    @Schema(description = "所属空间ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long spaceId;

    @NotBlank(message = "项目名称不能为空")
    @Size(max = 64, message = "项目名称长度不能超过64")
    @Schema(description = "项目/渠道/知识库名称", example = "后端项目A")
    private String name;

    @NotNull(message = "项目类型不能为空")
    @Schema(description = "类型: 1项目(Project), 2渠道(Channel), 3知识库(Wiki)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;
}
