package io.higgus.lab.module.storage.service.common;


import io.higgus.lab.module.storage.dal.dataobject.ContentMetadataDO;
import io.higgus.lab.module.storage.dal.mysql.ContentMetadataMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Service
public class FileService {


    @Resource
    S3Client s3Client;
    @Resource
    RedisTemplate<String,Object> redisTemplate;

    private final String DefaultBucketName = "test";

    ContentMetadataMapper fileMateMapper;


    // 映射 fileId 和 StorageKey 的。
    // 但是其实大热点的 fileId 频繁使用的后续可以优化进入 Redis
    public String matchStorageKey(String fileId) {
        String key = CollabRedisKey.getFileMetadataById(fileId);
        try {
        // 有缓存的情况下直接返回
        Object value = redisTemplate.opsForValue().get(fileId);
        if (value instanceof ContentMetadataDO) {
            ContentMetadataDO value1 = (ContentMetadataDO) value;
            return value1.getStorageKey();
        }

        // 没有缓存的情况下
        ContentMetadataDO file = fileMateMapper.selectById(fileId);
        if (file != null ) {
            // 先写入缓存
            redisTemplate.opsForValue().set(key, file);
            return file.getStorageKey();
        }
        } catch (Exception e) {

            throw e;
        }
        return null;
    }


    // 不是面向 MySQL 的
    public byte[] downloadAsBytes(String storageKey) {
        try (ResponseInputStream<GetObjectResponse> res = download(storageKey) ) {
            return res.readAllBytes();
        } catch (IOException e) {
            log.error(" IO 异常");
            throw new RuntimeException("从 S3 下载文件失败" + storageKey, e);
        }
    }


    public ResponseInputStream<GetObjectResponse> download(String storageKey) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(DefaultBucketName)
                .key(storageKey)
                .build();
        return s3Client.getObject(request);
    }



}
