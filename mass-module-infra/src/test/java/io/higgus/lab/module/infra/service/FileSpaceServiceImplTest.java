package io.higgus.lab.module.infra.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileSpaceServiceImplTest {

    private final FileSpaceServiceImpl fileSpaceService = new FileSpaceServiceImpl();

    @Test
    void calculateMd5_shouldReturnConsistentHash() {
        // 给定 - 创建一个测试文件内容
        MockMultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "Hello, Redisson!".getBytes()
        );

        // 当 - 计算 MD5
        String md5 = fileSpaceService.calculateMd5(file);

        // 那么 - MD5 应该是一个32位的十六进制字符串
        assertNotNull(md5);
        assertEquals(32, md5.length());
        assertTrue(md5.matches("[0-9a-f]{32}"));

        // 验证同一个文件总是返回相同的 MD5
        String md5Again = fileSpaceService.calculateMd5(file);
        assertEquals(md5, md5Again);
    }

    @Test
    void calculateMd5_differentContentShouldHaveDifferentHash() {
        // 给定 - 两个不同的文件
        MockMultipartFile file1 = new MockMultipartFile(
                "file1.txt",
                "file1.txt",
                "text/plain",
                "Content A".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "file2.txt",
                "file2.txt",
                "text/plain",
                "Content B".getBytes()
        );

        // 当 - 计算各自的 MD5
        String md5_1 = fileSpaceService.calculateMd5(file1);
        String md5_2 = fileSpaceService.calculateMd5(file2);

        // 那么 - 两个 MD5 应该不同
        assertNotEquals(md5_1, md5_2);
    }

    @Test
    void calculateMd5_emptyFileShouldWork() {
        // 给定 - 空文件
        MockMultipartFile emptyFile = new MockMultipartFile(
                "empty.txt",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        // 当 - 计算 MD5
        String md5 = fileSpaceService.calculateMd5(emptyFile);

        // 那么 - 空文件的 MD5 是 d41d8cd98f00b204e9800998ecf8427e（标准 MD5 值）
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", md5);
    }

    @Test
    void calculateMd5_knownContent() {
        // 给定 - 已知内容的文件
        // MD5("test") = "098f6bcd4621d373cade4e832627b4f6"
        MockMultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "test".getBytes()
        );

        // 当 - 计算 MD5
        String md5 = fileSpaceService.calculateMd5(file);

        // 那么 - 验证标准 MD5 值
        assertEquals("098f6bcd4621d373cade4e832627b4f6", md5);
    }
}
