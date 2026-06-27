package io.higgus.lab.module.storage.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String LOG_EXCHANGE = "collab.log.exchange";
    public static final String QUEUE_REDIS = "collab.log.queue.redis";
    public static final String QUEUE_MYSQL = "collab.log.queue.mysql";
    public static final String QUEUE_MINIO = "collab.log.queue.minio";

    // 声明消息序列化及反序列化处理
    @Bean
    MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 声明队列
    @Bean
    public Queue redisQueue() {
        return new Queue(QUEUE_REDIS, true);
    }
    @Bean
    public Queue mysqlQueue() {
        return new Queue(QUEUE_MYSQL, true);
    }
    @Bean
    public Queue minioQueue() {
        return new Queue(QUEUE_MINIO, true);
    }

    // 声明交换机
    @Bean
    public FanoutExchange logExchange() {
        return new FanoutExchange(LOG_EXCHANGE, true, false);
    }
    // 声明绑定关系
    @Bean
    public Binding bindRedis(Queue redisQueue, FanoutExchange logExchange) {
        return BindingBuilder.bind(redisQueue).to(logExchange);
    }
    @Bean
    public Binding bindMysql(Queue mysqlQueue, FanoutExchange logExchange) {
        return BindingBuilder.bind(mysqlQueue).to(logExchange);
    }
    @Bean
    public Binding bindMinio(Queue minioQueue, FanoutExchange logExchange) {
        return BindingBuilder.bind(minioQueue).to(logExchange);
    }
}
