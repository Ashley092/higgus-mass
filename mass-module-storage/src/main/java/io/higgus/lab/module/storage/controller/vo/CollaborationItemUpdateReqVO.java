package io.higgus.lab.module.storage.controller.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollaborationItemUpdateReqVO {

    @NotNull(message = "项目ID不能为空")
    private Long id;

    private Long spaceId;

    @Size(max = 64, message = "项目名称长度不能超过64")
    private String name;

    private Integer type;
}
