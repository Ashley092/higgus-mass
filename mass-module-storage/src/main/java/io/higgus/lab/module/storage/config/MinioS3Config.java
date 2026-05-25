package io.higgus.lab.module.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import java.net.URI;

@Configuration
public class MinioS3Config {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                // 1. 指向MinIO服务地址
                .endpointOverride(URI.create(endpoint))
                // 2. 设置认证凭证
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                // 3. 强制使用路径样式访问（MinIO要求）
                .forcePathStyle(true)
                // 4. 区域（本地环境可随意填）
                .region(Region.of(region))
                .build();
    }
}