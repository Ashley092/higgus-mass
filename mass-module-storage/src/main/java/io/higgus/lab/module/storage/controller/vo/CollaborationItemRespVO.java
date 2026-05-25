package io.higgus.lab.module.storage.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationItemRespVO {

    private Long id;

    private Long spaceId;

    private String name;

    private Integer type;

    private String creator;

    private String createTime;

    private String updater;

    private String updateTime;
}
