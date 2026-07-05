package io.higgus.lab.module.storage.service.edition.impl;

import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;
import io.higgus.lab.module.storage.service.edition.CollaborationEditFacade;
import io.higgus.lab.module.storage.service.edition.EditionLogService;
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
    @Autowired
    private EditionLogService editionLogService;

    @Override
    public void saveEdition(EditionExcelSaveReqVO reqVO) {
        messagingService.routeEditionLogToRealtimeQueue(reqVO);

    }

    @Override
    public void handleRealtimeEdition(Object cellEditDTO) {
        try {
            // 写入 Redis 缓存
            redisService.writeCellEdition(cellEditDTO);

            // 广播给其他用户
            redisService.tryBroadcastToUsers(cellEditDTO);

            // 发送消息进行持久化
            messagingService.routeEditionLogToPersistQueue(cellEditDTO);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void handlePersistEdition(Object dto) {
        try {

//            editionLogService.appendLog(dto);

        } catch (Exception e) {
            throw e;
        }
    }
}
