package io.higgus.lab.module.data.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgus.lab.module.data.common.constant.DataConstants;
import io.higgus.lab.module.data.common.message.UnifiedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 *
 * <p>负责从 RabbitMQ 消费消息并转发到业务系统
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    /**
     * 消费采集数据消息
     */
    @RabbitListener(queues = DataConstants.QUEUE_COLLECTION, concurrency = "3-10")
    public void handleCollectionMessage(Message message) {
        try {
            UnifiedMessage unifiedMessage = parseMessage(message);
            if (unifiedMessage == null) {
                log.error("解析消息失败，忽略");
                return;
            }

            log.info("消费采集数据: deviceId={}, messageId={}, timestamp={}",
                    unifiedMessage.getDeviceId(),
                    unifiedMessage.getMessageId(),
                    unifiedMessage.getTimestamp());

            // 这里可以添加业务处理逻辑
            // 例如：调用业务系统的 API
            processCollectionData(unifiedMessage);

        } catch (Exception e) {
            log.error("消费采集数据消息失败: {}", e.getMessage(), e);
            // 消息会被重新入队或进入死信队列
            throw new RuntimeException("消费消息失败", e);
        }
    }

    /**
     * 消费状态变更消息
     */
    @RabbitListener(queues = DataConstants.QUEUE_STATUS, concurrency = "2-5")
    public void handleStatusMessage(Message message) {
        try {
            UnifiedMessage unifiedMessage = parseMessage(message);
            if (unifiedMessage == null) {
                log.error("解析消息失败，忽略");
                return;
            }

            log.info("消费状态变更: deviceId={}, messageId={}",
                    unifiedMessage.getDeviceId(),
                    unifiedMessage.getMessageId());

            processStatusChange(unifiedMessage);

        } catch (Exception e) {
            log.error("消费状态变更消息失败: {}", e.getMessage(), e);
            throw new RuntimeException("消费消息失败", e);
        }
    }

    /**
     * 消费告警消息
     */
    @RabbitListener(queues = DataConstants.QUEUE_ALARM, concurrency = "2-5")
    public void handleAlarmMessage(Message message) {
        try {
            UnifiedMessage unifiedMessage = parseMessage(message);
            if (unifiedMessage == null) {
                log.error("解析消息失败，忽略");
                return;
            }

            log.warn("消费告警: deviceId={}, messageId={}, payload={}",
                    unifiedMessage.getDeviceId(),
                    unifiedMessage.getMessageId(),
                    unifiedMessage.getPayloadText());

            processAlarm(unifiedMessage);

        } catch (Exception e) {
            log.error("消费告警消息失败: {}", e.getMessage(), e);
            throw new RuntimeException("消费消息失败", e);
        }
    }

    /**
     * 解析消息
     */
    private UnifiedMessage parseMessage(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), UnifiedMessage.class);
        } catch (Exception e) {
            log.error("解析消息失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 处理采集数据
     *
     * <p>将数据转发到业务系统（如 bulong 模块）
     */
    private void processCollectionData(UnifiedMessage message) {
        // TODO: 实现转发到业务系统的逻辑
        // 例如：调用 RestTemplate 或 FeignClient 转发到 bulong 模块
        log.debug("处理采集数据: deviceId={}, payload={}", message.getDeviceId(), message.getPayloadText());

        // 示例：转发到 HTTP API
        // restTemplate.postForObject("http://bulong-service/admin/bulong/machine-production/create",
        //         message.getPayloadText(), CommonResult.class);
    }

    /**
     * 处理状态变更
     */
    private void processStatusChange(UnifiedMessage message) {
        // TODO: 实现状态变更处理逻辑
        log.debug("处理状态变更: deviceId={}, payload={}", message.getDeviceId(), message.getPayloadText());
    }

    /**
     * 处理告警
     */
    private void processAlarm(UnifiedMessage message) {
        // TODO: 实现告警处理逻辑
        // 例如：发送邮件、短信、钉钉通知等
        log.debug("处理告警: deviceId={}, payload={}", message.getDeviceId(), message.getPayloadText());
    }
}
