package io.higgus.lab.module.storage.mq.listener;


import io.higgus.lab.module.storage.config.RabbitMQConfig;
import io.higgus.lab.module.storage.service.CollabRedisService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Component
public class CollabEditChainListener {

    private final RabbitTemplate rabbitTemplate;

//    说是不建议用 @Resource 字段注入，用构造器注入比较好。
//    有挺多说法的。不过业务层我暂时先用简单的 Spring 注入
    @Resource
    CollabRedisService redisService;

    public CollabEditChainListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    final Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitListener(queues = RabbitMQConfig.Q_REDIS)
    public void handleRedisLog(Object logDto) {
        logger.info("[1] 实时广播阶段， Redis 队列监测到协同请求，开始处理实时广播");
        try {
            // 1. 向 redis 中写入对应的修改记录
             redisService.writeCellEdition(logDto);
            // 2. 广播此修改
            // redisService.tryBroadcastToUsers();

            // 如果上面步骤全部成功，投递该讯息至下一个交换机，进行持久化落盘
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_PERSIST, logDto);

        } catch (Exception e) {
            log.error("某一部实时传输阶段的工作失败了，拒绝发送 ACK ……");
            throw e;
        }
    }

}
