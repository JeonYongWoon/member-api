package com.example.memberapi.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // TODO: 회원 이름 필드 선언
    private String name;

    // TODO: 회원 나이 필드 선언
    private Integer age;

    // TODO: 회원 MBTI 필드 선언
    private String mbti;

    protected Member() {
    }

    // TODO: 생성자 작성
    public Member(String name, Integer age, String mbti) {
        this.name = name;
        this.age = age;
        this.mbti = mbti;
    }
    // TODO: 정적 생성 메서드 작성 여부 결정
    public static Member of(
            String name,
            Integer age,
            String mbti
    ) {
        return new Member(name, age, mbti);
    }
    // TODO: getter 메서드 작성

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getMbti() {
        return mbti;
    }
}