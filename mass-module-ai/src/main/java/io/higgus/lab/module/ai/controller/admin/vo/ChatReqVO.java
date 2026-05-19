package io.higgus.lab.module.ai.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - AI 对话请求 VO")
@Data
public class ChatReqVO {

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好，请介绍一下自己")
    @NotBlank(message = "消息内容不能为空")
    private String message;

    @Schema(description = "模型名称，默认使用配置中的默认模型", example = "llama3.2")
    private String model;

    @Schema(description = "对话历史（用于多轮对话）")
    private List<ChatMessage> history;

    @Data
    public static class ChatMessage {
        @Schema(description = "角色：user 表示用户，assistant 表示助手", example = "user")
        private String role;

        @Schema(description = "消息内容", example = "你好")
        private String content;
    }
}
