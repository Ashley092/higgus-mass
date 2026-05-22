package io.higgus.lab.module.infra.service;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FileUploadFacadeImpl implements FileUploadFacade {

    @Resource
    private final RedissonClient redissonClient;
    @Resource
    private final TransactionTemplate transactionTemplate;
    @Resource
    private final FileSpaceService fileSpaceService;

    // 无参构造函数，用于测试
    public FileUploadFacadeImpl() {
        this.redissonClient = null;
        this.transactionTemplate = null;
        this.fileSpaceService = null;
    }

    public FileUploadFacadeImpl(RedissonClient redissonClient,
                                TransactionTemplate transactionTemplate,
                                FileSpaceService fileSpaceService) {
        this.redissonClient = redissonClient;
        this.transactionTemplate = transactionTemplate;
        this.fileSpaceService = fileSpaceService;
    }


    public void executeUpload(MultipartFile file, Long userId, Long parentId ) throws InterruptedException {
        String md5 = fileSpaceService.calculateMd5(file);
        String lockKey = "lock:file:upload:" + md5;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(5, -1, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RuntimeException("文件正在处理中，请稍后再试");
            }
            transactionTemplate.execute( status -> {
                try {
                    log.info("外层事务 active: {}", TransactionSynchronizationManager.isActualTransactionActive());
                    fileSpaceService.doUploadBusiness(file, md5, userId, parentId);
                } catch (Exception e) {
                    status.setRollbackOnly(); // 异常情况回滚
                    throw new RuntimeException("上传处理失败...", e);
                }
                return null;
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("上传被中断" , e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

}
