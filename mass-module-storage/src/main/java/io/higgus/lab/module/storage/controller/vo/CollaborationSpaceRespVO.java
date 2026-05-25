package io.higgus.lab.module.storage.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationSpaceRespVO {

    private Long id;

    private String name;

    private String description;

    private Integer status;

    private String creator;

    private String createTime;

    private String updater;

    private String updateTime;
}
