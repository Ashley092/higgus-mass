package io.higgus.lab.module.data.config;

import io.higgus.lab.module.data.common.constant.DataConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final DataProperties dataProperties;

    // ==================== 交换机 ====================

    /**
     * 采集数据交换机
     */
    @Bean
    public TopicExchange collectionExchange() {
        return new TopicExchange(dataProperties.getMq().getExchange().getCollection(), true, false);
    }

    /**
     * 状态变更交换机
     */
    @Bean
    public TopicExchange statusExchange() {
        return new TopicExchange(dataProperties.getMq().getExchange().getStatus(), true, false);
    }

    /**
     * 告警交换机
     */
    @Bean
    public TopicExchange alarmExchange() {
        return new TopicExchange(dataProperties.getMq().getExchange().getAlarm(), true, false);
    }

    // ==================== 队列 ====================

    /**
     * 采集数据队列
     */
    @Bean
    public Queue collectionQueue() {
        return QueueBuilder.durable(dataProperties.getMq().getQueue().getCollection())
                .withArgument("x-dead-letter-exchange", dataProperties.getMq().getExchange().getCollection() + ".dlx")
                .withArgument("x-dead-letter-routing-key", "data.collection.dead")
                .build();
    }

    /**
     * 状态变更队列
     */
    @Bean
    public Queue statusQueue() {
        return QueueBuilder.durable(dataProperties.getMq().getQueue().getStatus())
                .withArgument("x-dead-letter-exchange", dataProperties.getMq().getExchange().getStatus() + ".dlx")
                .withArgument("x-dead-letter-routing-key", "data.status.dead")
                .build();
    }

    /**
     * 告警队列
     */
    @Bean
    public Queue alarmQueue() {
        return QueueBuilder.durable(dataProperties.getMq().getQueue().getAlarm())
                .withArgument("x-dead-letter-exchange", dataProperties.getMq().getExchange().getAlarm() + ".dlx")
                .withArgument("x-dead-letter-routing-key", "data.alarm.dead")
                .build();
    }

    // ==================== 绑定 ====================

    /**
     * 采集数据队列绑定
     */
    @Bean
    public Binding collectionBinding() {
        return BindingBuilder
                .bind(collectionQueue())
                .to(collectionExchange())
                .with(dataProperties.getMq().getRoutingKey().getCollection());
    }

    /**
     * 状态变更队列绑定
     */
    @Bean
    public Binding statusBinding() {
        return BindingBuilder
                .bind(statusQueue())
                .to(statusExchange())
                .with(dataProperties.getMq().getRoutingKey().getStatus());
    }

    /**
     * 告警队列绑定
     */
    @Bean
    public Binding alarmBinding() {
        return BindingBuilder
                .bind(alarmQueue())
                .to(alarmExchange())
                .with(dataProperties.getMq().getRoutingKey().getAlarm());
    }

    // ==================== 死信队列 ====================

    /**
     * 采集数据死信交换机
     */
    @Bean
    public DirectExchange collectionDlxExchange() {
        return new DirectExchange(dataProperties.getMq().getExchange().getCollection() + ".dlx");
    }

    /**
     * 采集数据死信队列
     */
    @Bean
    public Queue collectionDeadQueue() {
        return QueueBuilder.durable(dataProperties.getMq().getQueue().getCollection() + ".dead").build();
    }

    /**
     * 采集数据死信绑定
     */
    @Bean
    public Binding collectionDeadBinding() {
        return BindingBuilder
                .bind(collectionDeadQueue())
                .to(collectionDlxExchange())
                .with("data.collection.dead");
    }

    // ==================== 消息转换器 ====================

    /**
     * JSON 消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate 配置
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setMandatory(true);

        // 消息确认回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("消息发送失败: correlationData={}, cause={}", correlationData, cause);
            }
        });

        // 消息返回回调
        template.setReturnsCallback(returned -> {
            log.error("消息返回: exchange={}, routingKey={}, replyCode={}, replyText={}",
                    returned.getExchange(), returned.getRoutingKey(),
                    returned.getReplyCode(), returned.getReplyText());
        });

        return template;
    }

    /**
     * RabbitMQ 监听器容器工厂
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
