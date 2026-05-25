package io.higgus.lab.module.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelStorageService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private MinioBucketService bucketService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 上传Excel文件
     * @param file 上传的文件
     * @param folderPath 文件夹路径（如 "2024/12/"）
     * @return 对象Key
     */
    public String uploadExcel(MultipartFile file, String folderPath) throws IOException {
        // 确保桶存在
        bucketService.createBucketIfNotExists();

        // 构建对象Key（支持模拟目录结构）
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String objectKey = folderPath + timestamp + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return objectKey;
    }

    /**
     * 下载Excel文件
     */
    public InputStream downloadExcel(String objectKey) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        return s3Client.getObject(request);
    }

    /**
     * 删除Excel文件
     */
    public void deleteExcel(String objectKey) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.deleteObject(request);
    }

    /**
     * 列出桶中的所有文件
     */
    public List<String> listAllExcels() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        return response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
}

