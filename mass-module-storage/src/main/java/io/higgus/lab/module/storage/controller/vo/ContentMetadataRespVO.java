package io.higgus.lab.module.storage.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentMetadataRespVO {

    private Long id;

    private Long itemId;

    private String title;

    private Integer contentType;

    private String storageKey;

    private Long fileSize;

    private String fileExtension;

    private String fileMd5;

    private Integer version;

    private Long creator;

    private String creatorName;

    private Long updater;

    private String updaterName;

    private String createTime;

    private String updateTime;
}
