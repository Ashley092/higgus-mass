package io.higgus.lab.module.storage.service.mq;


import io.higgus.lab.module.storage.config.RabbitMQConfig;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollabMessagingService {

    private final RabbitTemplate rabbitTemplate;

    public CollabMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void routeEditionLogToRealtimeQueue(EditionExcelSaveLogDto dto) {
        // 对于任何一个修改，第一步先进入 Redis 队列等待广播。广播 ACK 后，串行进入下一个交换机
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EX_REALTIME,
                RabbitMQConfig.KEY_REALTIME,
                dto
        );
    }

    public void routeEditionLogToPersistQueue(Object cellDto) {
        // 递送消息给 Persist 队列
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EX_PERSIST,
                cellDto
        );
    }
}
