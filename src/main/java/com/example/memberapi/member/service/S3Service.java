package com.example.memberapi.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    // application.yml에 적어둔 버킷 이름을 가져옵니다.
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 우리가 S3Config에서 만든 도구 2개를 스프링이 알아서 넣어줍니다.
    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    /**
     * 1. 파일 업로드 메서드
     * MultipartFile을 받아서 S3에 올리고, 저장된 '파일 이름(Key)'을 반환합니다.
     */
    public String uploadImage(MultipartFile file) {
        // 똑같은 이름의 사진이 올라오면 덮어씌워지는 걸 막기 위해 파일명 앞에 랜덤 문자열(UUID)을 붙입니다.
        String fileName = "profiles/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            // S3에 "이 버킷에, 이 이름으로, 이런 타입의 파일을 넣을게" 라고 요청서 작성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // S3Client 도구를 이용해 진짜로 파일을 쏩니다!
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // 무사히 올라갔다면 DB에 저장하기 위해 파일 이름(Key)을 반환합니다.
            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
        }
    }

    /**
     * 2. Presigned URL (임시 티켓) 발급 메서드
     * DB에 저장되어 있던 '파일 이름(Key)'을 주면, 7일짜리 접근 URL을 만들어줍니다.
     */
    public String getPresignedUrl(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        // "7일 동안만 유효한 티켓을 만들어줘" 라고 요청서 작성 (🚨 과제 필수 요구사항 반영)
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7))
                .getObjectRequest(b -> b.bucket(bucketName).key(fileName))
                .build();

        // S3Presigner 도구를 이용해 임시 티켓(URL) 발급!
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        // 완성된 인터넷 주소(URL) 글자를 반환합니다.
        return presignedRequest.url().toString();
    }
}