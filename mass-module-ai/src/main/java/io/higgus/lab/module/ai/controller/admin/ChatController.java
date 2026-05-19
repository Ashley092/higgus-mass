package io.higgus.lab.module.ai.controller.admin;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.ai.controller.admin.vo.ChatReqVO;
import io.higgus.lab.module.ai.controller.admin.vo.ChatRespVO;
import io.higgus.lab.module.ai.service.ChatService;
import io.higgus.lab.module.ai.service.StreamingChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Tag(name = "AI 对话")
@RestController
@RequestMapping("/admin/ai/chat")
@Validated
public class ChatController {

    @Resource
    private ChatService chatService;

    @Resource
    private StreamingChatService streamingChatService;

    @Operation(summary = "对话", description = "向 AI 发送消息并获取回复")
    @PostMapping("/chat")
    public CommonResult<ChatRespVO> chat(@RequestBody @Valid ChatReqVO chatReqVO) {
        ChatRespVO result = chatService.chat(chatReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "流式对话", description = "向 AI 发送消息并获取流式回复 (SSE)")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void chatStream(@RequestBody @Valid ChatReqVO chatReqVO, HttpServletResponse response) {
        System.out.println("收到流式对话请求: " +  chatReqVO);
        try {
            response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Connection", "keep-alive");

            System.out.println("开始流式对话...");
            streamingChatService.streamChat(chatReqVO, response.getOutputStream());
            System.out.println("流式对话完成");
        } catch (Exception e) {
            System.out.println("流式对话异常" + e);
            try {
                response.getWriter().write("data: [ERROR] " + e.getMessage() + "\n\n");
                response.getWriter().flush();
            } catch (Exception ex) {
                System.out.println ("错误输出失败" + ex);
                System.out.println ("错误输出失败" + ex);
            }
        }
    }

    @Operation(summary = "获取支持的模型列表")
    @GetMapping("/models")
    public CommonResult<String[]> getModels() {
        return CommonResult.success(new String[]{"llama3.2"});
    }
}
