package io.higgus.lab.module.storage.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContentMetadataCreateReqVO {

    @NotNull(message = "所属项目ID不能为空")
    private Long itemId;

    @NotBlank(message = "内容标题不能为空")
    @Size(max = 128, message = "内容标题长度不能超过128")
    private String title;

    @NotNull(message = "内容类型不能为空")
    private Integer contentType;

    @Size(max = 255, message = "存储键长度不能超过255")
    private String storageKey;

    private Long fileSize;

    @Size(max = 16, message = "文件后缀长度不能超过16")
    private String fileExtension;

//    @Size(max = 32, message = "文件MD5长度不能超过32")
//    private String fileMd5;
}
