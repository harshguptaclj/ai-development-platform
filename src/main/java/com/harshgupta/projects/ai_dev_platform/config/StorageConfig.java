package com.harshgupta.projects.ai_dev_platform.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
@Slf4j
public class StorageConfig {

    private String url;
    private String accessKey;
    private String secretKey;
    private String projectBucket;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public ApplicationRunner ensureProjectBucket(MinioClient minioClient) {
        return args -> {
            try {
                if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(projectBucket).build())) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(projectBucket).build());
                    log.info("Created MinIO bucket '{}'", projectBucket);
                }
            } catch (Exception e) {
                log.error("Could not verify or create MinIO bucket '{}' at {}", projectBucket, url, e);
            }
        };
    }
}
