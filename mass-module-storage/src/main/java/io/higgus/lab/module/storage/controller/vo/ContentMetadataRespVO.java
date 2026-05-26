package io.higgus.lab.module.storage.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "内容元数据响应")
public class ContentMetadataRespVO {

    @Schema(description = "内容元数据ID", example = "1")
    private Long id;

    @Schema(description = "所属第二层(项目/渠道/Wiki)ID", example = "1")
    private Long itemId;

    @Schema(description = "内容标题/文件名(用户看到的名字)", example = "项目文档.pdf")
    private String title;

    @Schema(description = "内容本体类型: 1文件(File), 2任务(Task), 3在线文档(Doc), 4表格(Sheet)", example = "1")
    private Integer contentType;

    @Schema(description = "MinIO中的Object Key", example = "files/2024/abc123.pdf")
    private String storageKey;

    @Schema(description = "文件大小(Byte)", example = "1024000")
    private Long fileSize;

    @Schema(description = "文件后缀名(如: pdf, docx, png)", example = "pdf")
    private String fileExtension;

    @Schema(description = "文件MD5值", example = "d41d8cd98f00b204e9800998ecf8427e")
    private String fileMd5;

    @Schema(description = "乐观锁版本号", example = "1")
    private Integer version;

    @Schema(description = "创建者/上传者ID", example = "10001")
    private Long creator;

    @Schema(description = "创建者名称", example = "张三")
    private String creatorName;

    @Schema(description = "最后修改者ID", example = "10001")
    private Long updater;

    @Schema(description = "最后修改者名称", example = "张三")
    private String updaterName;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private String createTime;

    @Schema(description = "更新时间", example = "2024-01-01 10:00:00")
    private String updateTime;
}
