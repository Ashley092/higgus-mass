package io.higgus.lab.module.storage.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollaborationSpaceCreateReqVO {

    @NotBlank(message = "空间名称不能为空")
    @Size(max = 64, message = "空间名称长度不能超过64")
    private String name;

    @Size(max = 255, message = "空间描述长度不能超过255")
    private String description;
}
