package io.higgus.lab.module.ai.service;

import io.higgus.lab.module.ai.controller.admin.vo.ChatReqVO;
import java.io.OutputStream;

/**
 * AI 流式对话 Service 接口
 */
public interface StreamingChatService {

    /**
     * 流式发送对话请求
     */
    void streamChat(ChatReqVO chatReqVO, OutputStream outputStream);
}
