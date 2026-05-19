package io.higgus.lab.module.ai.service.impl;

import io.higgus.lab.module.ai.controller.admin.vo.ChatReqVO;
import io.higgus.lab.module.ai.service.StreamingChatService;
import io.higgus.lab.module.ai.tools.MachineQueryTools;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
public class StreamingChatServiceImpl implements StreamingChatService {

    @Resource
    private OllamaChatModel ollamaChatModel;

    @Resource
    private MachineQueryTools machineQueryTools;

    private static final String DEFAULT_MODEL = "llama3.2";

    private static final String SYSTEM_PROMPT = """
# 角色定义 [ROLE]
你是布隆经编制造业的**生产管理助手**，专注于帮助车间管理人员查询和分析生产数据。

你的核心能力：
• 查询机台状态和生产记录
• 分析生产效率和产能利用率
• 识别生产异常和延误风险
• 提供生产调度建议

---

# 输出格式规范 [OUTPUT_FORMAT] - 非常重要！

## 必须使用换行
你的输出**必须使用换行符**来组织内容，保持良好的可读性：

1. **段落之间必须换行**：不同的内容块之间用空行分隔
2. **列表项必须换行**：每个列表项单独一行
3. **标题后面换行**：标题和内容之间换行
4. **适当使用空行**：让内容层次分明

## 示例格式

好的格式（使用换行）：
```
您好！我是 AI 助手。

我可以帮助您：
• 查询机台状态
• 分析生产数据
• 提供调度建议

请问有什么可以帮助您的？
```

不好的格式（没有换行）：
```
您好！我是 AI 助手。我可以帮助您：• 查询机台状态• 分析生产数据• 提供调度建议请问有什么可以帮助您的？
```

## 约束条件 [CONSTRAINTS]

1. **必须换行**：输出内容必须合理使用换行符，提高可读性
2. **数字使用阿拉伯数字**，单位使用中文
3. **保持输出格式稳定**

---

# 领域知识补充 [DOMAIN_KNOWLEDGE]

### 经编机台状态说明
• **RUNNING**：机台正在执行生产任务
• **IDLE**：机台空闲，可分配新任务
• **STOPPED**：机台停机，可能是故障或等待物料
• **MAINTENANCE**：机台在进行计划保养

### 疪点类型参考
• **横条**：张力不均导致，检查盘头张力
• **油针**：油污导致，清洁织针
• **断纱**：张力过大或纱线质量问题
• **毛丝**：磨损或温控问题

### 常用术语
• **克重**：单位面积布料质量（g/m²）
• **门幅**：布料宽度（cm）
• **密度**：单位长度的线圈数
""";

    @Override
    public void streamChat(ChatReqVO chatReqVO, OutputStream outputStream) {
        log.info("StreamingChatService.streamChat 被调用, message: {}", chatReqVO.getMessage());
        log.info("历史消息数量: {}", chatReqVO.getHistory() != null ? chatReqVO.getHistory().size() : 0);
        CountDownLatch latch = new CountDownLatch(1);

        try {
            ChatClient chatClient = ChatClient.builder(ollamaChatModel).build();

            // 构建消息列表
            List<Message> messages = new ArrayList<>();

            // 添加历史消息（如果有多轮对话）
            if (chatReqVO.getHistory() != null && !chatReqVO.getHistory().isEmpty()) {
                messages.addAll(convertHistory(chatReqVO.getHistory()));
            }

            // 添加用户消息
            messages.add(new UserMessage(chatReqVO.getMessage()));

            // 流式调用
            chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .messages(messages)
                    .tools(machineQueryTools)
                    .stream()
                    .content()
                    .subscribe(
                            chunk -> {
                                try {
                                    synchronized (outputStream) {
                                        // 确保 chunk 不为 null
                                        String content = (chunk != null) ? chunk : "";
                                        // 将换行符替换为占位符，避免与 SSE 消息分隔符冲突
                                        // 修复流式响应中 Markdown 换行符丢失的问题
                                        String escapedContent = content.replace("\n", "<|NEWLINE|>");
                                        String data = "data: " + escapedContent + "\n\n";
                                        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
                                        outputStream.flush();
                                    }
                                } catch (Exception e) {
                                    log.error("流式输出失败", e);
                                }
                            },
                            error -> {
                                try {
                                    synchronized (outputStream) {
                                        String errorMsg = "data: [ERROR] " + error.getMessage() + "\n\n";
                                        outputStream.write(errorMsg.getBytes(StandardCharsets.UTF_8));
                                        outputStream.flush();
                                    }
                                } catch (Exception e) {
                                    log.error("错误输出失败", e);
                                }
                                latch.countDown();
                            },
                            () -> {
                                try {
                                    synchronized (outputStream) {
                                        String done = "data: [DONE]\n\n";
                                        outputStream.write(done.getBytes(StandardCharsets.UTF_8));
                                        outputStream.flush();
                                    }
                                } catch (Exception e) {
                                    log.error("完成信号输出失败", e);
                                }
                                latch.countDown();
                            }
                    );

            latch.await();

        } catch (Exception e) {
            log.error("AI 流式对话失败", e);
            try {
                synchronized (outputStream) {
                    String errorMsg = "data: [ERROR] " + e.getMessage() + "\n\n";
                    outputStream.write(errorMsg.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
            } catch (Exception ex) {
                log.error("错误输出失败", ex);
            }
        }
    }

    /**
     * 转换历史消息格式
     */
    private List<Message> convertHistory(List<ChatReqVO.ChatMessage> history) {
        List<Message> messages = new ArrayList<>();
        for (ChatReqVO.ChatMessage msg : history) {
            if ("user".equals(msg.getRole())) {
                messages.add(new UserMessage(msg.getContent()));
            } else {
                messages.add(new AssistantMessage(msg.getContent()));
            }
        }
        return messages;
    }
}
