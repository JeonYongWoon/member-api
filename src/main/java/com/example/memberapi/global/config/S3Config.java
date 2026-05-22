package com.example.memberapi.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    // 1. S3에 파일을 업로드할 때 사용하는 클라이언트
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create()) // IAM Role을 자동으로 읽어옵니다!
                .build();
    }

    // 2. Presigned URL(임시 접근 티켓)을 만들 때 사용하는 클라이언트
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}