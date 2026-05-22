package com.example.memberapi.member.service;

import com.example.memberapi.member.dto.MemberRequestDto;
import com.example.memberapi.member.dto.MemberResponseDto;
import com.example.memberapi.member.entity.Member;
import com.example.memberapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    // ⭐️ 1. S3 일꾼(S3Service)을 추가로 불러옵니다.
    private final S3Service s3Service;

    // ⭐️ 2. 생성자에도 S3Service를 추가해서 스프링이 알아서 주입하게 만듭니다.
    public MemberService(MemberRepository memberRepository, S3Service s3Service) {
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
    }

    // --- 기존 메서드 유지 ---
    public Long save(MemberRequestDto requestDto) {
        Member member = Member.of(
                requestDto.getName(),
                requestDto.getAge(),
                requestDto.getMbti()
        );
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public MemberResponseDto findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return new MemberResponseDto(member);
    }

    // --- 🚀 3단계 신규 기능 추가 ---

    /**
     * 기능 1: 프로필 이미지 업로드
     */
    @Transactional // DB 값을 변경(Update)하므로 트랜잭션을 걸어줍니다.
    public String uploadProfileImage(Long memberId, MultipartFile file) {
        // 1. 멤버가 진짜 있는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 2. S3 일꾼에게 사진을 넘기고, 저장된 '파일 이름'을 받아옴
        String fileName = s3Service.uploadImage(file);

        // 3. 멤버 엔티티에 파일 이름(주소) 업데이트 후 DB 저장
        member.updateProfileImageUrl(fileName);
        memberRepository.save(member);

        return fileName;
    }

    /**
     * 기능 2: 프로필 이미지 임시 티켓(Presigned URL) 발급
     */
    public String getProfileImagePresignedUrl(Long memberId) {
        // 1. 멤버가 진짜 있는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 2. DB에 저장된 파일 이름 가져오기
        String fileName = member.getProfileImageUrl();

        // 3. 만약 사진을 올린 적이 없다면 예외 처리
        if (fileName == null) {
            throw new IllegalArgumentException("프로필 이미지가 존재하지 않습니다.");
        }

        // 4. S3 일꾼에게 파일 이름을 주고, 7일짜리 URL 티켓을 받아와서 반환
        return s3Service.getPresignedUrl(fileName);
    }
}