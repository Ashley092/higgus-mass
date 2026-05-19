package io.higgus.lab.module.ai.service;

import io.higgus.lab.module.ai.controller.admin.vo.ChatReqVO;
import io.higgus.lab.module.ai.controller.admin.vo.ChatRespVO;

/**
 * AI 对话 Service 接口
 */
public interface ChatService {

    /**
     * 发送对话请求
     */
    ChatRespVO chat(ChatReqVO chatReqVO);
}
