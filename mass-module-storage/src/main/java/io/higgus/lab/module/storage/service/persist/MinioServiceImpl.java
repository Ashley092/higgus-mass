package io.higgus.lab.module.storage.service.persist;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private S3Client s3Client;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 检查桶是否存在
     */
    public boolean bucketExists() {
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

    /**
     * 创建桶（如果不存在）
     */
    public void createBucketIfNotExists() {
        if (!bucketExists()) {
            CreateBucketRequest request = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(request);
            System.out.println("Bucket '" + bucketName + "' created successfully.");
        }
    }

}
