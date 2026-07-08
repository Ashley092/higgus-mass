package io.higgus.lab.module.storage.service.redis;


import io.higgus.lab.module.storage.service.common.CollabRedisKey;
import io.higgus.lab.module.storage.service.common.FileServiceImpl;
import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Slf4j
@Service
public class CollabRedisService {

    @Resource
    private RedisTemplate<String, EditionExcelSaveLogDto> redisTemplate;
    @Resource
    private FileServiceImpl fileServiceImpl;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public void writeCellEdition(EditionExcelSaveLogDto dto) {
        String key = CollabRedisKey.CELL_EDIT_LOG + dto.getContentId();

        try {

            redisTemplate.opsForValue().set(key, dto);

        } catch (Exception e) {

            throw e;
        }
    }

    /**
     *  后续实现滑动窗口？
     * @param dto
     */
    public void tryBroadcastToUsers(EditionExcelSaveLogDto dto) {
        String key = CollabRedisKey.CELL_EDIT_LOG + dto.getContentId();

        try {
            redisTemplate.opsForValue().get(key);
            logger.info("获得数据 {} ", dto);

        } catch (Exception e) {

        }

    }

    public void trySendCompleteFileToUser() {

        //

    }




}
