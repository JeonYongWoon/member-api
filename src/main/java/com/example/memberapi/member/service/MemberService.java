// MemberService.java
package com.example.memberapi.member.service;

import com.example.memberapi.member.dto.MemberRequestDto;
import com.example.memberapi.member.dto.MemberResponseDto;
import com.example.memberapi.member.entity.Member;
import com.example.memberapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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
}