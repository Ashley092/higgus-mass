package io.higgus.lab.module.data.protocol.handler;

import io.higgus.lab.module.data.common.constant.DataConstants;
import io.higgus.lab.module.data.common.message.UnifiedMessage;

/**
 * 协议处理器接口
 *
 * <p>所有协议处理器都实现此接口，
 * 负责将特定协议的数据转换为统一消息格式。
 */
public interface ProtocolHandler {

    /**
     * 获取支持的协议类型
     */
    DataConstants.ProtocolType getProtocol();

    /**
     * 初始化处理器
     */
    default void initialize() {
        // 默认空实现
    }

    /**
     * 处理接收到的数据
     *
     * @param rawData 原始数据
     * @return 统一消息
     */
    UnifiedMessage handle(byte[] rawData);

    /**
     * 处理接收到的字符串数据
     *
     * @param rawData 原始字符串
     * @return 统一消息
     */
    default UnifiedMessage handle(String rawData) {
        return handle(rawData.getBytes());
    }

    /**
     * 发送数据（如果有响应）
     *
     * @param message 统一消息
     * @return 响应数据
     */
    default byte[] send(UnifiedMessage message) {
        return new byte[0];
    }

    /**
     * 发送字符串响应
     *
     * @param message 统一消息
     * @return 响应字符串
     */
    default String sendText(UnifiedMessage message) {
        byte[] response = send(message);
        return response.length > 0 ? new String(response) : "";
    }

    /**
     * 是否支持发送响应
     */
    default boolean supportResponse() {
        return false;
    }

    /**
     * 关闭处理器，释放资源
     */
    default void close() {
        // 默认空实现
    }
}
