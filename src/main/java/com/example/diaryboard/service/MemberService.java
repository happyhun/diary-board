package com.example.diaryboard.service;

import com.example.diaryboard.dto.LoginRequest;
import com.example.diaryboard.dto.LoginResponse;
import com.example.diaryboard.dto.SignupRequest;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.global.jwt.JwtProvider;
import com.example.diaryboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Long signup(SignupRequest dto) {
        if (memberRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("이미 가입된 이메일입니다.");

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .build();

        return memberRepository.save(member).getId();
    }

    public LoginResponse login(LoginRequest dto) {
        Optional<Member> member = memberRepository.findByEmail(dto.getEmail());

        if (member.isEmpty())
            throw new RuntimeException("가입되지 않은 이메일입니다.");

        if (!passwordEncoder.matches(dto.getPassword(), member.get().getPassword()))
            throw new RuntimeException("잘못된 비밀번호입니다.");

        String subject = String.valueOf(member.get().getId());
        String accessToken = jwtProvider.generateAccessToken(subject);
        String refreshToken = jwtProvider.generateRefreshToken(subject);

        return new LoginResponse(accessToken, refreshToken);
    }
}
