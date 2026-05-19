package io.higgus.lab.module.ai.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - AI 对话响应 VO")
@Data
public class ChatRespVO {

    @Schema(description = "模型名称", example = "llama3.2")
    private String model;

    @Schema(description = "回复内容", example = "你好！我是 Llama 3.2，一个由 Meta 开发的 AI 助手...")
    private String content;

    @Schema(description = "是否成功", example = "true")
    private Boolean success;

    @Schema(description = "错误信息（如果失败）", example = "")
    private String errorMessage;
}
