package io.higgus.lab.module.storage.mq;


import io.higgus.lab.module.storage.config.RabbitMQConfig;
import io.higgus.lab.module.storage.service.edition.CollaborationEditFacade;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CollaborationEditChainListener {

    private final CollaborationEditFacade editFacade;

//    说是不建议用 @Resource 字段注入，用构造器注入比较好。
//    有挺多说法的。不过业务层我暂时先用简单的 Spring 注入

    public CollaborationEditChainListener(CollaborationEditFacade editFacade) {
        this.editFacade = editFacade;
    }

    @RabbitListener(queues = RabbitMQConfig.Q_REDIS)
    public void handleRedisLog(EditionExcelSaveLogDto dto) {
        // 调用 编排逻辑服务层 的方法
        editFacade.handleRealtimeEdition(dto);

    }

    @RabbitListener(queues = RabbitMQConfig.Q_MYSQL)
    public void handleMysqlPersistLog(EditionExcelSaveLogDto dto) {
        try {
            editFacade.handleMysqlEdition(dto);
        } catch (IllegalArgumentException | NullPointerException e) {
            // 业务校验失败：直接丢弃消息，不再重投（不阻塞队列）
            log.error("[MQ] 收到无效消息，已丢弃。idempotentKey={}, cause={}",
                    dto == null ? null : dto.getIdempotentKey(), e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("invalid message: " + e.getMessage(), e);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // 入库字段非法（NOT NULL 违反等）：同样不再重试
            log.error("[MQ] 数据完整性约束失败，已丢弃。idempotentKey={}, cause={}",
                    dto == null ? null : dto.getIdempotentKey(), e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("data integrity violation", e);
        }
    }
    @RabbitListener(queues = RabbitMQConfig.Q_MINIO)
    public void handleMinioPersistLog(EditionExcelSaveLogDto dto) {
        try {
            editFacade.handleMinioEdition(dto);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("[MQ] MinIO 收到无效消息，已丢弃。idempotentKey={}, cause={}",
                    dto == null ? null : dto.getIdempotentKey(), e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("invalid message: " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            // storageKey 为 null / meta 不存在等业务状态异常，同样丢弃
            log.error("[MQ] MinIO 业务状态异常，已丢弃。idempotentKey={}, cause={}",
                    dto == null ? null : dto.getIdempotentKey(), e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("invalid state: " + e.getMessage(), e);
        }
    }
}
