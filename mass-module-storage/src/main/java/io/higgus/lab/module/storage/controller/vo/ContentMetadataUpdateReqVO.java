package io.higgus.lab.module.storage.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "更新内容元数据请求")
public class ContentMetadataUpdateReqVO {

    @NotNull(message = "内容ID不能为空")
    @Schema(description = "内容ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "所属第二层(项目/渠道/Wiki)ID", example = "1")
    private Long itemId;

    @Size(max = 128, message = "内容标题长度不能超过128")
    @Schema(description = "内容标题/文件名(用户看到的名字)", example = "项目文档.pdf")
    private String title;

    @Schema(description = "内容本体类型: 1文件(File), 2任务(Task), 3在线文档(Doc), 4表格(Sheet)", example = "1")
    private Integer contentType;

    @Size(max = 255, message = "存储键长度不能超过255")
    @Schema(description = "MinIO中的Object Key", example = "files/2024/abc123.pdf")
    private String storageKey;

    @Schema(description = "文件大小(Byte)", example = "1024000")
    private Long fileSize;

    @Size(max = 16, message = "文件后缀长度不能超过16")
    @Schema(description = "文件后缀名(如: pdf, docx, png)", example = "pdf")
    private String fileExtension;

    @Size(max = 32, message = "文件MD5长度不能超过32")
    @Schema(description = "文件MD5值", example = "d41d8cd98f00b204e9800998ecf8427e")
    private String fileMd5;
}
