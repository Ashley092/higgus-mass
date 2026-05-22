package io.higgus.lab.module.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据模块配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "data")
public class DataProperties {

    // ==================== 网关配置 ====================
    private GatewayProperties gateway = new GatewayProperties();

    // ==================== MQ 配置 ====================
    private MqProperties mq = new MqProperties();

    // ==================== 消费者配置 ====================
    private ConsumerProperties consumer = new ConsumerProperties();

    // ==================== 限流配置 ====================
    private RateLimitProperties rateLimit = new RateLimitProperties();

    // ==================== 幂等性配置 ====================
    private IdempotentProperties idempotent = new IdempotentProperties();

    @Data
    public static class GatewayProperties {
        /** HTTP 配置 */
        private HttpProperties http = new HttpProperties();
        /** TCP 配置 */
        private TcpProperties tcp = new TcpProperties();
        /** MQTT 配置 */
        private MqttProperties mqtt = new MqttProperties();
        /** 串口配置 */
        private SerialProperties serial = new SerialProperties();
    }

    @Data
    public static class HttpProperties {
        /** 是否启用 */
        private boolean enabled = true;
        /** 端口 */
        private int port = 8081;
        /** 上下文路径 */
        private String contextPath = "/data";
    }

    @Data
    public static class TcpProperties {
        /** 是否启用 */
        private boolean enabled = false;
        /** 端口 */
        private int port = 9000;
        /** 最大连接数 */
        private int maxConnections = 1000;
    }

    @Data
    public static class MqttProperties {
        /** 是否启用 */
        private boolean enabled = false;
        /** Broker URL */
        private String brokerUrl = "tcp://localhost:1883";
        /** 客户端ID */
        private String clientId = "data-module";
        /** 用户名 */
        private String username;
        /** 密码 */
        private String password;
        /** 订阅主题 */
        private String[] topics = {"device/data/#"};
        /** QoS */
        private int qos = 1;
    }

    @Data
    public static class SerialProperties {
        /** 是否启用 */
        private boolean enabled = false;
        /** 串口名称 */
        private String portName = "COM1";
        /** 波特率 */
        private int baudRate = 115200;
        /** 数据位 */
        private int dataBits = 8;
        /** 停止位 */
        private int stopBits = 1;
        /** 校验位 */
        private int parity = 0;
    }

    @Data
    public static class MqProperties {
        /** 交换机配置 */
        private ExchangeProperties exchange = new ExchangeProperties();
        /** 队列配置 */
        private QueueProperties queue = new QueueProperties();
        /** 路由键配置 */
        private RoutingKeyProperties routingKey = new RoutingKeyProperties();
    }

    @Data
    public static class ExchangeProperties {
        private String collection = "data.collection.exchange";
        private String status = "data.status.exchange";
        private String alarm = "data.alarm.exchange";
    }

    @Data
    public static class QueueProperties {
        private String collection = "data.collection.queue";
        private String status = "data.status.queue";
        private String alarm = "data.alarm.queue";
    }

    @Data
    public static class RoutingKeyProperties {
        private String collection = "data.collection.#";
        private String status = "data.status.#";
        private String alarm = "data.alarm.#";
    }

    @Data
    public static class ConsumerProperties {
        /** 采集数据消费者配置 */
        private CollectionConsumerProperties collection = new CollectionConsumerProperties();
        /** 状态变更消费者配置 */
        private StatusConsumerProperties status = new StatusConsumerProperties();
        /** 告警消费者配置 */
        private AlarmConsumerProperties alarm = new AlarmConsumerProperties();
    }

    @Data
    public static class CollectionConsumerProperties {
        private boolean enabled = true;
        private int concurrency = 3;
        private int maxConcurrency = 10;
        private int prefetchCount = 10;
        private int retryTimes = 3;
        private long retryInterval = 1000;
    }

    @Data
    public static class StatusConsumerProperties {
        private boolean enabled = true;
        private int concurrency = 2;
        private int maxConcurrency = 5;
    }

    @Data
    public static class AlarmConsumerProperties {
        private boolean enabled = true;
        private int concurrency = 2;
        private int maxConcurrency = 5;
        /** 通知配置 */
        private NotificationProperties notification = new NotificationProperties();
    }

    @Data
    public static class NotificationProperties {
        private boolean enabled = false;
        private String webhookUrl;
    }

    @Data
    public static class RateLimitProperties {
        /** 启用限流 */
        private boolean enabled = true;
        /** 全局限流值（条/秒） */
        private int globalLimit = 1000;
        /** 单机台限流值（条/秒） */
        private int perDeviceLimit = 10;
        /** 滑动窗口大小（秒） */
        private int windowSize = 1;
    }

    @Data
    public static class IdempotentProperties {
        /** 启用幂等性 */
        private boolean enabled = true;
        /** 幂等性key过期时间（秒） */
        private int keyExpireSeconds = 300;
        /** 幂等性key前缀 */
        private String keyPrefix = "data:idempotent:";
    }
}
