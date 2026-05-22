package io.higgus.lab.module.infra.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadFacadeImplTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private FileSpaceService fileSpaceService;

    @Mock
    private RLock rLock;

    private FileUploadFacadeImpl fileUploadFacade;

    @BeforeEach
    void setUp() throws Exception {
        // 通过反射注入 mock 对象
        fileUploadFacade = new FileUploadFacadeImpl();

        // 反射设置 redissonClient
        Field redissonField = FileUploadFacadeImpl.class.getDeclaredField("redissonClient");
        redissonField.setAccessible(true);
        redissonField.set(fileUploadFacade, redissonClient);

        // 反射设置 transactionTemplate
        Field transactionField = FileUploadFacadeImpl.class.getDeclaredField("transactionTemplate");
        transactionField.setAccessible(true);
        transactionField.set(fileUploadFacade, transactionTemplate);

        // 反射设置 fileSpaceService
        Field fileSpaceField = FileUploadFacadeImpl.class.getDeclaredField("fileSpaceService");
        fileSpaceField.setAccessible(true);
        fileSpaceField.set(fileUploadFacade, fileSpaceService);
    }

    @Test
    void executeUpload_success() throws Exception {
        // 给定
        MockMultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "Hello".getBytes()
        );
        String expectedMd5 = "abc123";
        when(fileSpaceService.calculateMd5(file)).thenReturn(expectedMd5);
        when(redissonClient.getLock("lock:file:upload:" + expectedMd5)).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);
        // 模拟 transactionTemplate.execute 返回 null
        when(transactionTemplate.execute(any(TransactionCallback.class))).thenReturn(null);

        // 当 - 通过反射调用 private 方法
        Method executeUploadMethod = FileUploadFacadeImpl.class.getDeclaredMethod(
                "executeUpload",
                org.springframework.web.multipart.MultipartFile.class,
                Long.class,
                Long.class
        );
        executeUploadMethod.setAccessible(true);
        executeUploadMethod.invoke(fileUploadFacade, file, 1L, 0L);

        // 那么 - 验证各步骤按预期执行
        verify(fileSpaceService).calculateMd5(file);           // 1. 计算 MD5
        verify(redissonClient).getLock("lock:file:upload:" + expectedMd5);  // 2. 获取锁
        verify(rLock).tryLock(anyLong(), anyLong(), any());     // 3. 尝试获取锁
        verify(transactionTemplate).execute(any(TransactionCallback.class)); // 4. 执行事务
        verify(rLock).unlock();                                // 5. 释放锁
    }

    @Test
    void executeUpload_lockFailed() throws Exception {
        // 给定 - 锁获取失败
        MockMultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "Hello".getBytes()
        );
        when(fileSpaceService.calculateMd5(file)).thenReturn("md5");
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        // 当 & 那么 - 应该抛出异常
        Method executeUploadMethod = FileUploadFacadeImpl.class.getDeclaredMethod(
                "executeUpload",
                org.springframework.web.multipart.MultipartFile.class,
                Long.class,
                Long.class
        );
        executeUploadMethod.setAccessible(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            try {
                executeUploadMethod.invoke(fileUploadFacade, file, 1L, 0L);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
        assertEquals("文件正在处理中，请稍后再试", exception.getMessage());
        verify(transactionTemplate, never()).execute(any(TransactionCallback.class));  // 事务不应该执行
        verify(rLock, never()).unlock();  // 锁不应该被释放
    }

    @Test
    void executeUpload_lockNotHeldByCurrentThread() throws Exception {
        // 给定 - 锁没有被当前线程持有
        MockMultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "Hello".getBytes()
        );
        when(fileSpaceService.calculateMd5(file)).thenReturn("md5");
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(false);  // 关键：不是当前线程持有
        when(transactionTemplate.execute(any(TransactionCallback.class))).thenReturn(null);

        // 当
        Method executeUploadMethod = FileUploadFacadeImpl.class.getDeclaredMethod(
                "executeUpload",
                org.springframework.web.multipart.MultipartFile.class,
                Long.class,
                Long.class
        );
        executeUploadMethod.setAccessible(true);
        executeUploadMethod.invoke(fileUploadFacade, file, 1L, 0L);

        // 那么 - unlock 不应该被调用
        verify(rLock, never()).unlock();
    }

    @Test
    void executeUpload_businessException_shouldRollback() throws Exception {
        // 给定 - 业务逻辑抛出异常
        MockMultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "Hello".getBytes()
        );
        when(fileSpaceService.calculateMd5(file)).thenReturn("md5");
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        // 模拟 transactionTemplate.execute 抛出异常
        when(transactionTemplate.execute(any(TransactionCallback.class)))
                .thenThrow(new RuntimeException("业务异常"));

        // 当 & 那么 - 应该抛出异常
        Method executeUploadMethod = FileUploadFacadeImpl.class.getDeclaredMethod(
                "executeUpload",
                org.springframework.web.multipart.MultipartFile.class,
                Long.class,
                Long.class
        );
        executeUploadMethod.setAccessible(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            try {
                executeUploadMethod.invoke(fileUploadFacade, file, 1L, 0L);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
        // 验证异常信息包含业务异常
        assertTrue(exception.getMessage().contains("业务异常") || exception.getMessage().contains("上传处理失败"));
        // 验证 unlock 被调用了（无论成功还是异常）
        verify(rLock, atLeastOnce()).unlock();
    }
}
