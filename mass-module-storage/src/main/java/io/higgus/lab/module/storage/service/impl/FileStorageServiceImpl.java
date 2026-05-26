package io.higgus.lab.module.storage.service.impl;

import io.higgus.lab.module.storage.service.FileStorageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Resource
    private S3Client s3Client;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint:}")
    private String endpoint;

    @Override
    public String upload(MultipartFile file, String storageKey) throws IOException {
        if (!bucketExists()) {
            throw new IOException("Bucket '" + bucketName + "' does not exist");
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        log.info("文件上传成功, storageKey={}, size={}", storageKey, file.getSize());
        return storageKey;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        String storageKey = UUID.randomUUID().toString().replace("-", "");
        return upload(file, storageKey);
    }

    @Override
    public String upload(byte[] data, String storageKey) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .contentLength((long) data.length)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(data));
        log.info("字节数组上传成功, storageKey={}, size={}", storageKey, data.length);
        return storageKey;
    }

    @Override
    public void delete(String storageKey) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();
            s3Client.deleteObject(request);
            log.info("文件删除成功, storageKey={}", storageKey);
        } catch (NoSuchKeyException e) {
            log.warn("文件不存在，无需删除, storageKey={}", storageKey);
        }
    }

    @Override
    public ResponseInputStream<GetObjectResponse> download(String storageKey) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();
        return s3Client.getObject(request);
    }

    @Override
    public byte[] downloadAsBytes(String storageKey) {
        ResponseInputStream<GetObjectResponse> response = download(storageKey);
        try {
            return response.readAllBytes();
        } catch (IOException e) {
            log.error("读取文件字节失败, storageKey={}", storageKey, e);
            throw new RuntimeException("读取文件失败: " + storageKey, e);
        }
    }

    @Override
    public boolean exists(String storageKey) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();
            s3Client.headObject(request);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public String calculateMd5(MultipartFile file) throws IOException {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(file.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IOException("Failed to calculate MD5", e);
        }
    }

    @Override
    public String getDownloadUrl(String storageKey, int expirySeconds) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();
            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build()).toString();
        } catch (Exception e) {
            log.error("生成下载链接失败, storageKey={}", storageKey, e);
            return null;
        }
    }

    private boolean bucketExists() {
        try {
            HeadBucketRequest request = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(request);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }
}
