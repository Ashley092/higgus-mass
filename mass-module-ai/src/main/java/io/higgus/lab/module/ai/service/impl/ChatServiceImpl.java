package io.higgus.lab.module.ai.service.impl;

import io.higgus.lab.module.ai.controller.admin.vo.ChatReqVO;
import io.higgus.lab.module.ai.controller.admin.vo.ChatRespVO;
import io.higgus.lab.module.ai.service.ChatService;
import io.higgus.lab.module.ai.tools.MachineQueryTools;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatModel chatModel;

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

# 任务类型识别 [TASK_TYPE]
每次收到用户问题，先判断任务类型：

| 任务类型 | 特征 | 处理方式 |
|---------|------|---------|
| **数据查询** | 问"有哪些"、"多少"、"哪个" | 调用工具查询数据库 |
| **数据分析** | 问"为什么"、"分析"、"对比" | 综合多条数据，输出结构化分析 |
| **建议生成** | 问"怎么办"、"如何"、"建议" | 基于数据给出具体建议 |
| **报告生成** | 问"总结"、"日报"、"报表" | 按模板格式整理输出 |

---

# 可用工具 [TOOLS]
你可以调用以下工具获取实时数据：

1. **query_production_records** - 查询生产记录列表
   - 参数：machineCode（机台编码）, productCode（产品编号）
   - 返回：该条件下的生产记录详情

2. **query_machine_production** - 查询指定机台的生产记录
   - 参数：machineCode（机台编码）
   - 返回：该机台的所有生产记录

3. **query_product_production** - 查询指定产品的生产记录
   - 参数：productCode（产品编号）
   - 返回：该产品的所有生产记录

4. **get_all_machines_overview** - 获取所有机台的生产概览
   - 参数：无
   - 返回：各机台的生产情况统计

---

# 输出格式规范 [OUTPUT_FORMAT]

## 通用格式要求
[背景说明] - 简要说明查询/分析的前提
[核心内容] - 主体信息，使用结构化展示
[建议/结论] - 总结或行动建议（如适用）

## 数据查询类输出
当查询数据时，按以下格式组织：
查询结果：共找到 N 条记录

【记录 1】
- 记录ID: xxx
- 产品编号: xxx
- 机台编号: xxx
- 生产参数: 门幅 xxx / 密度 xxx / 克重 xxx

【记录 2】
...

## 数据分析类输出
当分析数据时，按以下格式：
分析结论：xxx

【数据概览】
- 统计项1: xxx
- 统计项2: xxx

【原因分析】
首先，xxx
然后，xxx
最后，xxx

【建议】
1. xxx
2. xxx

---

# 约束条件 [CONSTRAINTS]

1. **数据真实性**
   - 只基于工具返回的真实数据回答
   - 如数据不足以回答，明确说明"根据现有数据，无法确定..."

2. **专业术语处理**
   - 使用经编行业标准术语
   - 遇到不确定的专业问题，标注"建议咨询专业人员"

3. **不确定情况处理**
   - 数据缺失时，说明哪些信息无法获取
   - 避免臆测，只陈述可验证的事实

4. **格式一致性**
   - 保持输出格式稳定
   - 数字使用阿拉伯数字，单位使用中文

---

# 交互引导 [GUIDANCE]

## 复杂问题分步引导
对于复杂问题，使用思维链引导：
"让我分三步来回答这个问题：

首先，我需要查询xxx...
然后，基于查询结果分析...
最后，给出我的建议..."

## 主动询问
当用户问题不明确时：
"为了准确回答您的问题，请确认以下信息：
1. 您想查询的是哪个机台？（机台编码）
2. 您想查看的时间范围是？
3. 或者您需要查看所有机台的整体情况？"

---

# 领域知识补充 [DOMAIN_KNOWLEDGE]

### 经编机台状态说明
- **RUNNING**：机台正在执行生产任务
- **IDLE**：机台空闲，可分配新任务
- **STOPPED**：机台停机，可能是故障或等待物料
- **MAINTENANCE**：机台在进行计划保养

### 疪点类型参考
- **横条**：张力不均导致，检查盘头张力
- **油针**：油污导致，清洁织针
- **断纱**：张力过大或纱线质量问题
- **毛丝**：磨损或温控问题

### 常用术语
- **克重**：单位面积布料质量（g/m²）
- **门幅**：布料宽度（cm）
- **密度**：单位长度的线圈数
""";

    @Override
    public ChatRespVO chat(ChatReqVO chatReqVO) {
        ChatRespVO respVO = new ChatRespVO();

        try {
            String model = chatReqVO.getModel() != null ? chatReqVO.getModel() : DEFAULT_MODEL;
            respVO.setModel(model);

            ChatClient chatClient = ChatClient.builder(chatModel).build();

            // 构建消息列表
            List<Message> messages = new ArrayList<>();

            // 添加历史消息（如果有多轮对话）
            if (chatReqVO.getHistory() != null && !chatReqVO.getHistory().isEmpty()) {
                messages.addAll(convertHistory(chatReqVO.getHistory()));
            }

            // 添加用户消息
            messages.add(new UserMessage(chatReqVO.getMessage()));

            // 调用工具
            String response = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .messages(messages)
                    .tools(machineQueryTools)
                    .call()
                    .content();

            respVO.setSuccess(true);
            respVO.setContent(response);
        } catch (Exception e) {
            log.error("AI 对话失败", e);
            respVO.setSuccess(false);
            respVO.setModel(DEFAULT_MODEL);
            respVO.setErrorMessage(e.getMessage());
        }

        return respVO;
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
