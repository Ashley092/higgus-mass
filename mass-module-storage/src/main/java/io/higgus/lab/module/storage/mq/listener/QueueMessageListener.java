package io.higgus.lab.module.storage.mq.listener;


import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueueMessageListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    RabbitTemplate rabbitTemplate;


    static final String QUEUE_REDIS_EDIT = "q_redis_edit";
    static final String QUEUE_MYSQL_EDIT = "q_mysql_edit";
    static final String QUEUE_MINIO_EDIT = "q_minio_edit";

    @RabbitListener(queues = QUEUE_REDIS_EDIT)
    public void onEditionMessageFromRedisEditQueue(CellUpdateMessage message) throws Exception {
        logger.info("queue {} received edit message" , QUEUE_REDIS_EDIT);


    }

}
