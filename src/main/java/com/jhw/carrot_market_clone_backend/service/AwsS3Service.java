package com.jhw.carrot_market_clone_backend.service;

import io.awspring.cloud.core.region.StaticRegionProvider;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeletedObject;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

@Service
public class AwsS3Service {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final String bucketDomain = "https://d97irvrwc2w22.cloudfront.net/";

    private final S3Presigner s3Presigner;

    private final S3Client s3Client;

    public AwsS3Service(
            S3Presigner s3Presigner,
            S3Client s3Client
    ) {
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
    }

    public String createPreSignedUrlForUpload(
            String inputFileName,
            String inputContentType
    ) throws ServiceException {

        // PutObjectRequest : put 하고자 하는 객체에 대한 요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(inputFileName)
                .contentType(inputContentType)
                .build();

        // PutObjectPresignRequest : PutObjectRequest를 기반으로 pre-signed URL 생성
        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(preSignRequest).url().toString();
    }

    public void deleteObject(String filename) throws ServiceException {
        String objectPath = "/" + filename;
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder()
                .bucket(bucket)
                .key(objectPath)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
