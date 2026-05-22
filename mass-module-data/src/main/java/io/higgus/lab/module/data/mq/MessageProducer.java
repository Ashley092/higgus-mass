package io.higgus.lab.module.data.mq;

import io.higgus.lab.module.data.common.constant.DataConstants;
import io.higgus.lab.module.data.common.message.UnifiedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息发送器
 *
 * <p>负责将统一消息发送到 RabbitMQ
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送采集数据消息
     */
    public void sendCollectionMessage(UnifiedMessage message) {
        send(message, DataConstants.EXCHANGE_COLLECTION, DataConstants.ROUTING_KEY_COLLECTION);
    }

    /**
     * 发送状态变更消息
     */
    public void sendStatusMessage(UnifiedMessage message) {
        send(message, DataConstants.EXCHANGE_STATUS, DataConstants.ROUTING_KEY_STATUS);
    }

    /**
     * 发送告警消息
     */
    public void sendAlarmMessage(UnifiedMessage message) {
        send(message, DataConstants.EXCHANGE_ALARM, DataConstants.ROUTING_KEY_ALARM);
    }

    /**
     * 发送消息
     */
    public void send(UnifiedMessage message, String exchange, String routingKey) {
        try {
            // 设置消息属性
            MessageProperties properties = new MessageProperties();
            properties.setMessageId(message.getMessageId());
            properties.setTimestamp(new java.util.Date(message.getTimestamp()));
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

            // 设置消息头
            if (message.getDeviceId() != null) {
                properties.setHeader("deviceId", message.getDeviceId());
            }
            if (message.getType() != null) {
                properties.setHeader("messageType", message.getType().name());
            }

            // 发送消息
            rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
                msg.getMessageProperties().setHeaders(properties.getHeaders());
                return msg;
            });

            log.debug("消息发送成功: exchange={}, routingKey={}, messageId={}",
                    exchange, routingKey, message.getMessageId());

        } catch (Exception e) {
            log.error("消息发送失败: exchange={}, routingKey={}, messageId={}, error={}",
                    exchange, routingKey, message.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    /**
     * 发送消息（带延迟）
     */
    public void sendWithDelay(UnifiedMessage message, String exchange, String routingKey, long delayMillis) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, m -> {
                m.getMessageProperties().setDelay((int) delayMillis);
                return m;
            });

            log.debug("延迟消息发送成功: exchange={}, routingKey={}, delay={}ms, messageId={}",
                    exchange, routingKey, delayMillis, message.getMessageId());

        } catch (Exception e) {
            log.error("延迟消息发送失败: exchange={}, routingKey={}, delay={}ms, error={}",
                    exchange, routingKey, delayMillis, e.getMessage());
            throw new RuntimeException("延迟消息发送失败", e);
        }
    }
}
