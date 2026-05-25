package io.higgus.lab.module.storage.controller.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContentMetadataUpdateReqVO {

    @NotNull(message = "内容ID不能为空")
    private Long id;

    private Long itemId;

    @Size(max = 128, message = "内容标题长度不能超过128")
    private String title;

    private Integer contentType;

    @Size(max = 255, message = "存储键长度不能超过255")
    private String storageKey;

    private Long fileSize;

    @Size(max = 16, message = "文件后缀长度不能超过16")
    private String fileExtension;

    @Size(max = 32, message = "文件MD5长度不能超过32")
    private String fileMd5;
}
