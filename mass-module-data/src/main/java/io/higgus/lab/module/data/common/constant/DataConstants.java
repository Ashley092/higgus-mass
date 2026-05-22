package io.higgus.lab.module.data.common.constant;

/**
 * 数据模块常量定义
 */
public interface DataConstants {

    // ==================== 消息类型 ====================

    /**
     * 消息类型枚举
     */
    enum MessageType {
        /** 采集数据 */
        COLLECTION_DATA,
        /** 状态变更 */
        STATUS_CHANGE,
        /** 告警 */
        ALARM,
        /** 心跳 */
        HEARTBEAT,
        /** 控制命令 */
        CONTROL_COMMAND,
        /** 配置下发 */
        CONFIG_UPDATE
    }

    // ==================== 协议类型 ====================

    /**
     * 协议类型枚举
     */
    enum ProtocolType {
        /** HTTP/REST */
        HTTP,
        /** TCP 自定义协议 */
        TCP,
        /** RS232 串口 */
        RS232,
        /** RS485 串口 */
        RS485,
        /** MQTT */
        MQTT,
        /** WebSocket */
        WEBSOCKET,
        /** 文件导入 */
        FILE,
        /** 未知 */
        UNKNOWN
    }

    // ==================== 编码类型 ====================

    /**
     * 编码类型枚举
     */
    enum EncodingType {
        /** JSON */
        JSON,
        /** XML */
        XML,
        /** Protocol Buffers */
        PROTOBUF,
        /** 自定义二进制 */
        CUSTOM_BINARY,
        /** CSV */
        CSV,
        /** 纯文本 */
        TEXT
    }

    // ==================== 设备状态 ====================

    /**
     * 设备状态枚举
     */
    enum DeviceStatus {
        /** 离线 */
        OFFLINE,
        /** 空闲 */
        IDLE,
        /** 运行中 */
        RUNNING,
        /** 暂停 */
        PAUSED,
        /** 告警 */
        ALARM,
        /** 故障 */
        FAULT,
        /** 维护 */
        MAINTENANCE
    }

    // ==================== MQ 常量 ====================

    /** 采集数据交换机 */
    String EXCHANGE_COLLECTION = "data.collection.exchange";

    /** 状态变更交换机 */
    String EXCHANGE_STATUS = "data.status.exchange";

    /** 告警交换机 */
    String EXCHANGE_ALARM = "data.alarm.exchange";

    /** 采集数据队列 */
    String QUEUE_COLLECTION = "data.collection.queue";

    /** 状态变更队列 */
    String QUEUE_STATUS = "data.status.queue";

    /** 告警队列 */
    String QUEUE_ALARM = "data.alarm.queue";

    /** 采集数据路由键 */
    String ROUTING_KEY_COLLECTION = "data.collection.#";

    /** 状态变更路由键 */
    String ROUTING_KEY_STATUS = "data.status.#";

    /** 告警路由键 */
    String ROUTING_KEY_ALARM = "data.alarm.#";

    // ==================== 通用常量 ====================

    /** 默认分隔符 */
    String DEFAULT_SEPARATOR = ",";

    /** 帧头标识 */
    short FRAME_HEADER = (short) 0xAA55;

    /** 心跳间隔（秒） */
    int HEARTBEAT_INTERVAL = 30;

    /** 连接超时（毫秒） */
    int CONNECT_TIMEOUT = 5000;

    /** 读超时（毫秒） */
    int READ_TIMEOUT = 10000;
}
