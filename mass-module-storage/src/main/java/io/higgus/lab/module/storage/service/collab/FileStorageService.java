package io.higgus.lab.module.storage.service.collab;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

public interface FileStorageService {

    /**
     * 上传文件到 MinIO
     *
     * @param file     文件
     * @param storageKey 存储键（通常用 MD5 或 UUID）
     * @return 实际存储的 key
     */
    String upload(MultipartFile file, String storageKey) throws IOException;

    /**
     * 上传文件（自动生成存储键）
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * 上传字节数组到 MinIO（用于覆盖 Excel 文件）
     *
     * @param data       字节数据
     * @param storageKey 存储键
     * @return 实际存储的 key
     */
    String upload(byte[] data, String storageKey);

    /**
     * 删除文件
     *
     * @param storageKey 存储键
     */
    void delete(String storageKey);

    /**
     * 下载文件
     *
     * @param storageKey 存储键
     * @return 文件流
     */
    ResponseInputStream<GetObjectResponse> download(String storageKey);

    /**
     * 下载文件为字节数组
     *
     * @param storageKey 存储键
     * @return 文件字节数据
     */
    byte[] downloadAsBytes(String storageKey);

    /**
     * 检查文件是否存在
     *
     * @param storageKey 存储键
     * @return 是否存在
     */
    boolean exists(String storageKey);

    /**
     * 获取文件元数据（包含真实文件大小）
     *
     * @param storageKey 存储键
     * @return 文件元数据响应
     */
    software.amazon.awssdk.services.s3.model.HeadObjectResponse headObject(String storageKey);

    /**
     * 计算文件的 MD5
     *
     * @param file 文件
     * @return MD5 值
     */
    String calculateMd5(MultipartFile file) throws IOException;

    /**
     * 获取文件下载链接
     *
     * @param storageKey 存储键
     * @param expirySeconds 过期时间（秒）
     * @return 下载链接
     */
    String getDownloadUrl(String storageKey, int expirySeconds);
}
