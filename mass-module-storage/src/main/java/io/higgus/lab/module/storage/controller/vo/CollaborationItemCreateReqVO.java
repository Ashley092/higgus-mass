package io.higgus.lab.module.storage.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollaborationItemCreateReqVO {

    @NotNull(message = "空间ID不能为空")
    private Long spaceId;

    @NotBlank(message = "项目名称不能为空")
    @Size(max = 64, message = "项目名称长度不能超过64")
    private String name;

    @NotNull(message = "项目类型不能为空")
    private Integer type;
}
