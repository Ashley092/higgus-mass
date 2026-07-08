package io.higgus.lab.module.storage.service.edition.impl;

import io.higgus.lab.module.storage.controller.edition.vo.EditionExcelSaveReqVO;
import io.higgus.lab.module.storage.convert.edition.EditionExcelSaveLogConvert;
import io.higgus.lab.module.storage.service.edition.CollaborationEditFacade;
import io.higgus.lab.module.storage.service.edition.EditionLogService;
import io.higgus.lab.module.storage.service.edition.EditionPoiService;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import io.higgus.lab.module.storage.service.mq.CollabMessagingService;
import io.higgus.lab.module.storage.service.redis.CollabRedisService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class CollaborationEditFacadeImpl implements CollaborationEditFacade {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CollabRedisService redisService;
    @Autowired
    private CollabMessagingService messagingService;
    @Autowired
    private EditionLogService editionLogService;
    @Autowired
    private EditionPoiService poiService;

    private static final EditionExcelSaveLogConvert editionCovert = EditionExcelSaveLogConvert.INSTANCE;

    @Override
    public void saveEdition(EditionExcelSaveReqVO reqVO) {
        logger.info("ContentId 为 {} 请求已传入，调用 Edition 函数", reqVO.getContentId());
        EditionExcelSaveLogDto dto = editionCovert.toEditionLogDto(reqVO);
        dto.setUpdater(1999L);
        dto.setCreateTime(LocalDateTime.now());
        dto.setIdempotentKey(generateIdempotentKey(dto));
        messagingService.routeEditionLogToRealtimeQueue(dto);
    }

    @Override
    public void handleRealtimeEdition(EditionExcelSaveLogDto dto) {
        try {
            logger.info("[2] 实时阶段传输 {} ",dto.getContentId());
            // 写入 Redis 缓存
            redisService.writeCellEdition(dto);

            // 广播给其他用户
            redisService.tryBroadcastToUsers(dto);

            // 发送消息进行持久化
            messagingService.routeEditionLogToPersistQueue(dto);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void handleMysqlEdition(EditionExcelSaveLogDto dto) {
        try {
            logger.info("[3] 修改尝试写入 MySQL ");
            editionLogService.appendLog(dto);


            messagingService.routeEditionLogToSnapshotQueue(dto);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void handleMinioEdition(EditionExcelSaveLogDto dto) {
        try {

            logger.info("[4] 快照版本更新");
            poiService.tryGenerateNewSnapshotFile(dto);


        } catch (Exception e) {
            throw e;
        }
    }


    private String generateIdempotentKey(EditionExcelSaveLogDto dto) {
        String content = dto.getContentId() + ":" + dto.getRowIndex() + ":" + dto.getColIndex() + ":"
                + dto.getNewValue();
        String timeWindow = dto.getCreateTime().format(DateTimeFormatter.ofPattern("yyyMMddHHmm"));
        return "cell:" + content.hashCode() + ":" + timeWindow;
    }
}
