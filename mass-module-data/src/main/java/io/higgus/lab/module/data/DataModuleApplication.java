package io.higgus.lab.module.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 数据接入模块启动类
 *
 * <p>该模块负责：
 * <ul>
 *   <li>多种协议的数据接入（HTTP/TCP/RS232/MQTT）</li>
 *   <li>消息队列处理（RabbitMQ）</li>
 *   <li>数据转换与标准化</li>
 *   <li>流量控制与限流</li>
 *   <li>消息幂等性处理</li>
 * </ul>
 *
 * <p>设计原则：
 * <ul>
 *   <li>与业务系统解耦 - 数据接入与业务处理分离</li>
 *   <li>协议无关 - 统一的消息格式</li>
 *   <li>可插拔 - 协议处理器可动态添加</li>
 *   <li>高可用 - 消息持久化、重试机制</li>
 * </ul>
 */
@SpringBootApplication(scanBasePackages = "io.higgus.lab.module.data")
@EnableConfigurationProperties
public class DataModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataModuleApplication.class, args);
    }
}
