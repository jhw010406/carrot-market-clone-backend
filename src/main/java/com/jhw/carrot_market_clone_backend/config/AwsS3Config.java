package com.jhw.carrot_market_clone_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsS3Config {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static}")
    private Region region;

    @Bean
    public S3Presigner s3Presigner() {
         AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                 AwsBasicCredentials.create(accessKey, secretKey)
         );

         return S3Presigner.builder()
                 .region(Region.AP_NORTHEAST_2)
                 .credentialsProvider(credentialsProvider)
                 .build();
    }

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        return S3Client.builder()
                .credentialsProvider(provider)
                .region(region)
                .build();
    }
}
