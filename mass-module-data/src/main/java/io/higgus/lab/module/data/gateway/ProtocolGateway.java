package io.higgus.lab.module.data.gateway;

import io.higgus.lab.module.data.common.constant.DataConstants;
import io.higgus.lab.module.data.common.message.UnifiedMessage;
import io.higgus.lab.module.data.mq.MessageProducer;
import io.higgus.lab.module.data.protocol.handler.ProtocolHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 协议适配网关
 *
 * <p>统一入口，负责：
 * <ul>
 *   <li>管理多种协议处理器</li>
 *   <li>消息分发与路由</li>
 *   <li>流量控制</li>
 *   <li>消息转换</li>
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProtocolGateway {

    private final List<ProtocolHandler> protocolHandlers;
    private final MessageProducer messageProducer;

    /** 协议处理器映射 */
    private Map<DataConstants.ProtocolType, ProtocolHandler> handlerMap;

    @PostConstruct
    public void init() {
        // 初始化处理器映射
        handlerMap = protocolHandlers.stream()
                .collect(Collectors.toMap(
                        ProtocolHandler::getProtocol,
                        h -> h,
                        (h1, h2) -> h1
                ));

        log.info("协议网关初始化完成，支持的协议: {}", handlerMap.keySet());

        // 初始化各处理器
        protocolHandlers.forEach(handler -> {
            try {
                handler.initialize();
                log.info("协议处理器初始化成功: {}", handler.getProtocol());
            } catch (Exception e) {
                log.error("协议处理器初始化失败: {}, error: {}", handler.getProtocol(), e.getMessage());
            }
        });
    }

    /**
     * 处理消息
     */
    public UnifiedMessage handle(DataConstants.ProtocolType protocol, byte[] data) {
        ProtocolHandler handler = handlerMap.get(protocol);
        if (handler == null) {
            log.warn("不支持的协议类型: {}", protocol);
            return createUnknownProtocolMessage(data);
        }
        return handler.handle(data);
    }

    /**
     * 处理消息（字符串形式）
     */
    public UnifiedMessage handle(DataConstants.ProtocolType protocol, String data) {
        return handle(protocol, data.getBytes());
    }

    /**
     * 处理并发送消息到 MQ
     */
    public void handleAndPublish(DataConstants.ProtocolType protocol, byte[] data) {
        UnifiedMessage message = handle(protocol, data);
        publishMessage(message);
    }

    /**
     * 发布消息到 MQ
     */
    public void publishMessage(UnifiedMessage message) {
        if (message == null) {
            log.warn("消息为空，跳过发布");
            return;
        }

        try {
            switch (message.getType()) {
                case COLLECTION_DATA:
                    messageProducer.sendCollectionMessage(message);
                    break;
                case STATUS_CHANGE:
                    messageProducer.sendStatusMessage(message);
                    break;
                case ALARM:
                    messageProducer.sendAlarmMessage(message);
                    break;
                case HEARTBEAT:
                    log.debug("心跳消息，不发布: deviceId={}", message.getDeviceId());
                    break;
                default:
                    messageProducer.sendCollectionMessage(message);
            }
        } catch (Exception e) {
            log.error("消息发布失败: messageId={}, error={}", message.getMessageId(), e.getMessage());
            throw new RuntimeException("消息发布失败", e);
        }
    }

    /**
     * 获取支持的协议列表
     */
    public List<DataConstants.ProtocolType> getSupportedProtocols() {
        return handlerMap.keySet().stream().collect(Collectors.toList());
    }

    private UnifiedMessage createUnknownProtocolMessage(byte[] data) {
        return UnifiedMessage.builder()
                .messageId(generateMessageId())
                .type(DataConstants.MessageType.COLLECTION_DATA)
                .deviceId("unknown")
                .timestamp(System.currentTimeMillis())
                .payload(data)
                .sourceProtocol(DataConstants.ProtocolType.UNKNOWN)
                .processed(false)
                .processResult("unknown protocol")
                .build();
    }

    private String generateMessageId() {
        return String.format("%d-%s",
                System.currentTimeMillis(),
                java.util.UUID.randomUUID().toString().substring(0, 8));
    }
}
