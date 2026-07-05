package io.higgus.lab.module.storage.mq;


import io.higgus.lab.module.storage.config.RabbitMQConfig;
import io.higgus.lab.module.storage.service.edition.CollaborationEditFacade;
import io.higgus.lab.module.storage.service.redis.CollabRedisService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Component
public class CollaborationEditChainListener {

    private final CollaborationEditFacade editFacade;

//    说是不建议用 @Resource 字段注入，用构造器注入比较好。
//    有挺多说法的。不过业务层我暂时先用简单的 Spring 注入
    @Resource
    CollabRedisService redisService;

    public CollaborationEditChainListener(CollaborationEditFacade editFacade) {
        this.editFacade = editFacade;
    }

    @RabbitListener(queues = RabbitMQConfig.Q_REDIS)
    public void handleRedisLog(Object logDto) {
        // 调用 编排逻辑服务层 的方法
        editFacade.handleRealtimeEdition(logDto);

    }

    @RabbitListener(queues = RabbitMQConfig.EX_PERSIST)
    public void handlePersistLog(Object logDto) {
        editFacade.handlePersistEdition(logDto);
    }

}
