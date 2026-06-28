package io.higgus.lab.module.storage.service;


import io.higgus.lab.module.storage.service.common.CollabRedisKey;
import io.higgus.lab.module.storage.service.common.FileService;
import io.higgus.lab.module.storage.service.excel.ExcelService;
import jakarta.annotation.Resource;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Service
public class CollabRedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ExcelService excelService;
    @Resource
    private FileService fileService;



    public void writeCellEdition(Object cellEdition) {
        String key = CollabRedisKey.CELL_EDIT_LOG;

        try {

            redisTemplate.opsForValue().set(key, cellEdition);

        } catch (Exception e) {

            throw e;
        }
    }

    public void tryBroadcastToUsers() {


        try {


        } catch (Exception e) {

        }

    }


    public void trySaveCompleteFile(String fileId) {
        try {
            String key = CollabRedisKey.FILE_SNAPSHOT + fileId;
            // 一般来说，就是获取最新的快照
            // 1. 先检查 Redis 里面有没有这个文件
            Object value = redisTemplate.opsForValue().get(key);
            if (value instanceof Sheet) {
                // 存在这个文件的快照
                // 这里后续要作新版本 or 旧版本判断
                return;
            }
            // 如果没有该文件
            // 缓存管理器中有吗？
            // [默认 Redis 管理对象的情况下] 先加载读取这个文件
            String storageKey = fileService.matchStorageKey(fileId);
            byte[] excelBytes = fileService.downloadAsBytes(storageKey);
            // 获得了这个 byte 形式的文件




        } catch (Exception e ) {

            log.error("存储进入 Redis 失败");
            throw e;
        }


    }

    public void trySendCompleteFileToUser() {

        //

    }




}
