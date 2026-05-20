// MemberController.java
package com.example.memberapi.member.controller;

import com.example.memberapi.member.dto.MemberRequestDto;
import com.example.memberapi.member.dto.MemberResponseDto;
import com.example.memberapi.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}