package io.higgus.lab.module.storage.service;


import io.higgus.lab.module.storage.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollabMessagingService {

    static final String QUEUE_REDIS_EDIT = "q_redis_edit";
    static final String QUEUE_MYSQL_EDIT = "q_mysql_edit";
    static final String QUEUE_MINIO_EDIT = "q_minio_edit";

    private final RabbitTemplate rabbitTemplate;

    public CollabMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void routeCellLogToMq(Object cellDto) {
        // 对于任何一个修改，第一步先进入 Redis 队列等待广播。广播 ACK 后，串行进入下一个交换机
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EX_REALTIME,
                RabbitMQConfig.KEY_REALTIME,
                cellDto
        );
    }
}
