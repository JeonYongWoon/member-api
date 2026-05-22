package com.example.memberapi.member.controller;

import com.example.memberapi.member.dto.MemberRequestDto;
import com.example.memberapi.member.dto.MemberResponseDto;
import com.example.memberapi.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // 레벨 3 사진 파일을 받기 위해 추가됨

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Long> createMember(@RequestBody MemberRequestDto requestDto) {
        Long id = memberService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        MemberResponseDto responseDto = memberService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    // --- 3단계 신규 기능 추가 ---

    /**
     * 1. 프로필 이미지 업로드 API
     */
    @PostMapping("/{id}/profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        String savedFileName = memberService.uploadProfileImage(id, file);
        return ResponseEntity.ok("이미지 업로드 성공! 파일명: " + savedFileName);
    }

    /**
     * 2. 프로필 이미지 임시 티켓(Presigned URL) 발급 API
     */
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<String> getProfileImage(@PathVariable Long id) {

        String presignedUrl = memberService.getProfileImagePresignedUrl(id);
        return ResponseEntity.ok(presignedUrl);
    }
}