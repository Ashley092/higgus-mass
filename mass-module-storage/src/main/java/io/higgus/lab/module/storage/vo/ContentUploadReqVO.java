package io.higgus.lab.module.storage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "文件上传请求")
public class ContentUploadReqVO {

    @NotNull(message = "所属项目ID不能为空")
    @Schema(description = "所属第二层(项目/渠道/Wiki)ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long itemId;

    @NotBlank(message = "内容标题不能为空")
    @Schema(description = "内容标题/文件名(用户看到的名字)", example = "项目文档.pdf")
    private String title;

    @NotNull(message = "内容类型不能为空")
    @Schema(description = "内容本体类型: 1文件(File), 2任务(Task), 3在线文档(Doc), 4表格(Sheet)", example = "1")
    private Integer contentType;
}
