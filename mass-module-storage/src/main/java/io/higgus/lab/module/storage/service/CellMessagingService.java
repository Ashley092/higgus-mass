package io.higgus.lab.module.storage.service;


import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CellMessagingService {


    static final String QUEUE_REDIS_EDIT = "q_redis_edit";
    static final String QUEUE_MYSQL_EDIT = "q_mysql_edit";
    static final String QUEUE_MINIO_EDIT = "q_minio_edit";

    @Resource
    RabbitTemplate rabbitTemplate;

    public void sendRedisUpdateMessage() {

    }
}
