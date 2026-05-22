package io.higgus.lab.module.data.common.message;

import io.higgus.lab.module.data.common.constant.DataConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 统一消息格式
 *
 * <p>所有协议的数据最终都转换为这个格式，确保内部处理的一致性。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 基础信息 ====================

    /** 消息唯一标识 */
    private String messageId;

    /** 消息类型 */
    private DataConstants.MessageType type;

    /** 设备/机台标识 */
    private String deviceId;

    /** 时间戳（毫秒） */
    private Long timestamp;

    // ==================== 数据内容 ====================

    /** 消息数据（JSON 字符串或二进制） */
    private byte[] payload;

    /** 消息数据（字符串形式，便于日志） */
    private String payloadText;

    // ==================== 元数据 ====================

    /** 元数据 */
    private Map<String, String> metadata;

    /** 原始协议 */
    private DataConstants.ProtocolType sourceProtocol;

    /** 编码类型 */
    private DataConstants.EncodingType encoding;

    /** 原始 IP 地址（用于 HTTP） */
    private String remoteAddress;

    // ==================== 处理状态 ====================

    /** 是否已处理 */
    private Boolean processed;

    /** 处理时间 */
    private Long processTime;

    /** 处理结果 */
    private String processResult;

    // ==================== 辅助方法 ====================

    /**
     * 创建采集数据消息
     */
    public static UnifiedMessage collectionData(String deviceId, byte[] payload) {
        return UnifiedMessage.builder()
                .messageId(generateMessageId())
                .type(DataConstants.MessageType.COLLECTION_DATA)
                .deviceId(deviceId)
                .timestamp(System.currentTimeMillis())
                .payload(payload)
                .payloadText(new String(payload))
                .sourceProtocol(DataConstants.ProtocolType.UNKNOWN)
                .encoding(DataConstants.EncodingType.JSON)
                .processed(false)
                .build();
    }

    /**
     * 创建状态变更消息
     */
    public static UnifiedMessage statusChange(String deviceId, DataConstants.DeviceStatus status) {
        return UnifiedMessage.builder()
                .messageId(generateMessageId())
                .type(DataConstants.MessageType.STATUS_CHANGE)
                .deviceId(deviceId)
                .timestamp(System.currentTimeMillis())
                .payload(("{\"status\":\"" + status.name() + "\"}").getBytes())
                .sourceProtocol(DataConstants.ProtocolType.UNKNOWN)
                .encoding(DataConstants.EncodingType.JSON)
                .processed(false)
                .build();
    }

    /**
     * 创建告警消息
     */
    public static UnifiedMessage alarm(String deviceId, String alarmCode, String alarmMessage) {
        return UnifiedMessage.builder()
                .messageId(generateMessageId())
                .type(DataConstants.MessageType.ALARM)
                .deviceId(deviceId)
                .timestamp(System.currentTimeMillis())
                .payload(("{\"alarmCode\":\"" + alarmCode + "\",\"alarmMessage\":\"" + alarmMessage + "\"}").getBytes())
                .sourceProtocol(DataConstants.ProtocolType.UNKNOWN)
                .encoding(DataConstants.EncodingType.JSON)
                .processed(false)
                .build();
    }

    /**
     * 创建心跳消息
     */
    public static UnifiedMessage heartbeat(String deviceId) {
        return UnifiedMessage.builder()
                .messageId(generateMessageId())
                .type(DataConstants.MessageType.HEARTBEAT)
                .deviceId(deviceId)
                .timestamp(System.currentTimeMillis())
                .sourceProtocol(DataConstants.ProtocolType.UNKNOWN)
                .processed(false)
                .build();
    }

    /**
     * 生成消息ID
     */
    private static String generateMessageId() {
        return String.format("%d-%s",
                System.currentTimeMillis(),
                java.util.UUID.randomUUID().toString().substring(0, 8));
    }

    /**
     * 获取元数据值
     */
    public String getMetadata(String key) {
        return metadata != null ? metadata.get(key) : null;
    }

    /**
     * 设置元数据值
     */
    public void setMetadata(String key, String value) {
        if (this.metadata == null) {
            this.metadata = new java.util.HashMap<>();
        }
        this.metadata.put(key, value);
    }
}
