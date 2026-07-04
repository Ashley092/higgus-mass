package io.higgus.lab.module.storage.service.edition;

import io.higgus.lab.module.storage.service.mq.CollabMessagingService;
import io.higgus.lab.module.storage.service.redis.CollabRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollaborationEditFacadeImpl implements CollaborationEditFacade {

    @Autowired
    private CollabRedisService redisService;
    @Autowired
    private CollabMessagingService messagingService;

    @Override
    public void handleCellEdit(Object cellEditDTO) {
        // 写入 Redis 缓存
        redisService.writeCellEdition(cellEditDTO);

        // 广播给其他用户
        redisService.broadcastToUsers();

        // 发送消息进行持久化
        messagingService.routeCellLogToMq(cellEditDTO);

    }
}
