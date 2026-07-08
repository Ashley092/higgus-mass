package io.higgus.lab.module.storage.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 实时层
    public static final String EX_REALTIME = "collab.ex.realtime";
    public static final String Q_REDIS = "collab.q.redis";
    public static final String KEY_REALTIME = "collab.key.realtime";

    // 持久化层
    public static final String EX_PERSIST_MYSQL = "collab.ex.persist.mysql";
    public static final String EX_PERSIST_MINIO = "collab.ex.persist.minio";
    public static final String Q_MYSQL = "collab.q.mysql";
    public static final String Q_MINIO = "collab.q.minio";
    public static final String KEY_PERSIST_MYSQL = "collab.key.persist.mysql";
    public static final String KEY_PERSIST_MINIO = "collab.key.persist.minio";


    // 声明消息序列化及反序列化处理
    @Bean
    MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 声明实时阶段组件
    @Bean
    public DirectExchange realtimeExchange() {
        return new DirectExchange(EX_REALTIME, true, false);
    }
    @Bean
    public Queue redisQueue() {
        return new Queue(Q_REDIS, true); // durable = true 队列持久化
    }
    @Bean
    public Binding bindRedis(Queue redisQueue, DirectExchange realtimeExchange) {
        return BindingBuilder.bind(redisQueue).to(realtimeExchange).with(KEY_REALTIME);
    }

    // 声明持久化部分组件
    @Bean
    public DirectExchange persistMysqlExchange() {
        return new DirectExchange(EX_PERSIST_MYSQL, true, false);
    }
    @Bean
    public DirectExchange persistMinioExchange() {
        return new DirectExchange(EX_PERSIST_MINIO, true, false);
    }
    @Bean
    public Queue mysqlQueue() {
        return new Queue(Q_MYSQL, true);
    }
    @Bean
    public Queue minioQueue() {
        return new Queue(Q_MINIO, true);
    }
    @Bean
    public Binding bindMysql(Queue mysqlQueue, DirectExchange persistMysqlExchange) {
        return BindingBuilder.bind(mysqlQueue).to(persistMysqlExchange).with(KEY_PERSIST_MYSQL);
    }
    @Bean
    public Binding bindMinio(Queue minioQueue, DirectExchange persistMinioExchange) {
        return BindingBuilder.bind(minioQueue).to(persistMinioExchange).with(KEY_PERSIST_MINIO);
    }

}
