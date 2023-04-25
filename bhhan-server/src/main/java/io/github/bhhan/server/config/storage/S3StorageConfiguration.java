package io.github.bhhan.server.config.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.bhhan.server.service.storage.s3.S3StorageServiceImpl;
import io.github.bhhan.server.service.storage.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@RequiredArgsConstructor
public class S3StorageConfiguration {
    @Value("${cloud.aws.credentials.accesskey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretkey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(region)
                .build();
    }

    @Bean
    public S3Uploader s3Uploader(AmazonS3Client amazonS3Client) {
        return new S3Uploader(amazonS3Client);
    }

    @Bean
    public S3StorageServiceImpl storageService(S3Uploader s3Uploader) {
        return new S3StorageServiceImpl(s3Uploader);
    }
}
