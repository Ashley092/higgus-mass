package io.higgus.lab.module.data.protocol.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgus.lab.module.data.common.constant.DataConstants;
import io.higgus.lab.module.data.common.message.UnifiedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * HTTP/JSON 协议处理器
 *
 * <p>处理标准 HTTP POST 请求，接收 JSON 格式的设备数据。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpJsonHandler implements ProtocolHandler {

    private final ObjectMapper objectMapper;

    @Override
    public DataConstants.ProtocolType getProtocol() {
        return DataConstants.ProtocolType.HTTP;
    }

    @Override
    public UnifiedMessage handle(byte[] rawData) {
        try {
            String jsonStr = new String(rawData);
            JsonNode root = objectMapper.readTree(jsonStr);

            // 提取设备ID
            String deviceId = extractDeviceId(root);
            if (deviceId == null) {
                log.warn("无法从消息中提取设备ID: {}", jsonStr);
                deviceId = "unknown";
            }

            // 提取消息类型
            DataConstants.MessageType messageType = extractMessageType(root);

            // 构建统一消息
            return UnifiedMessage.builder()
                    .messageId(generateMessageId())
                    .type(messageType)
                    .deviceId(deviceId)
                    .timestamp(System.currentTimeMillis())
                    .payload(rawData)
                    .payloadText(jsonStr)
                    .sourceProtocol(DataConstants.ProtocolType.HTTP)
                    .encoding(DataConstants.EncodingType.JSON)
                    .processed(false)
                    .build();

        } catch (Exception e) {
            log.error("解析 JSON 数据失败: {}", e.getMessage(), e);
            return UnifiedMessage.builder()
                    .messageId(generateMessageId())
                    .type(DataConstants.MessageType.COLLECTION_DATA)
                    .deviceId("parse-error")
                    .timestamp(System.currentTimeMillis())
                    .payload(rawData)
                    .sourceProtocol(DataConstants.ProtocolType.HTTP)
                    .encoding(DataConstants.EncodingType.JSON)
                    .processed(false)
                    .processResult("parse error: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public boolean supportResponse() {
        return true;
    }

    @Override
    public byte[] send(UnifiedMessage message) {
        try {
            // 构建响应
            ResponseBody response = new ResponseBody();
            response.success = true;
            response.messageId = message.getMessageId();
            response.message = "success";

            return objectMapper.writeValueAsBytes(response);
        } catch (Exception e) {
            log.error("构建响应失败: {}", e.getMessage());
            return "{\"success\":false}".getBytes();
        }
    }

    /**
     * 从 JSON 中提取设备ID
     */
    private String extractDeviceId(JsonNode root) {
        // 尝试多种可能的字段名
        String[] fields = {"deviceId", "device_id", "machineCode", "machine_code", "machineCode", "sn"};
        for (String field : fields) {
            JsonNode node = root.path(field);
            if (!node.isMissingNode() && !node.isNull()) {
                return node.asText();
            }
        }
        return null;
    }

    /**
     * 从 JSON 中提取消息类型
     */
    private DataConstants.MessageType extractMessageType(JsonNode root) {
        JsonNode typeNode = root.path("type");
        if (!typeNode.isMissingNode()) {
            String type = typeNode.asText().toUpperCase();
            try {
                return DataConstants.MessageType.valueOf(type);
            } catch (IllegalArgumentException e) {
                log.warn("未知的消息类型: {}", type);
            }
        }
        return DataConstants.MessageType.COLLECTION_DATA;
    }

    private String generateMessageId() {
        return String.format("%d-%s",
                System.currentTimeMillis(),
                java.util.UUID.randomUUID().toString().substring(0, 8));
    }

    /**
     * 响应体
     */
    @lombok.Data
    private static class ResponseBody {
        private boolean success;
        private String messageId;
        private String message;
    }
}
