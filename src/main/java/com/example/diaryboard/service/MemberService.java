package com.example.diaryboard.service;

import com.example.diaryboard.dto.SignupDto;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signup(SignupDto dto) {
        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .build();

        return memberRepository.save(member).getId();
    }
}
