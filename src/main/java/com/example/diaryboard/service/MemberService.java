package com.example.diaryboard.service;

import com.example.diaryboard.dto.LoginRequest;
import com.example.diaryboard.dto.LoginResponse;
import com.example.diaryboard.dto.ReissueResponse;
import com.example.diaryboard.dto.SignupRequest;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.global.jwt.JwtProvider;
import com.example.diaryboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.diaryboard.global.exception.ExceptionCode.*;
import static com.example.diaryboard.global.jwt.JwtConfig.SCOPE_REFRESH;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtDecoder jwtDecoder;

    public Long signup(SignupRequest dto) {
        if (memberRepository.existsByEmail(dto.getEmail()))
            throw new CustomException(DUPLICATED_EMAIL, "이미 가입된 이메일입니다");

        if (memberRepository.existsByNickname(dto.getNickname()))
            throw new CustomException(DUPLICATED_NICKNAME, "이미 가입된 닉네임입니다");

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
            throw new CustomException(UNAUTHORIZED_LOGIN, "가입되지 않은 이메일입니다");

        if (!passwordEncoder.matches(dto.getPassword(), member.get().getPassword()))
            throw new CustomException(UNAUTHORIZED_LOGIN, "틀린 비밀번호입니다");

        String subject = String.valueOf(member.get().getId());
        String accessToken = jwtProvider.generateAccessToken(subject);
        String refreshToken = jwtProvider.generateRefreshToken(subject);

        return new LoginResponse(accessToken, refreshToken);
    }

    public ReissueResponse reissue(String refreshToken) {
        System.out.println(refreshToken);
        Jwt jwt = jwtDecoder.decode(refreshToken);

        if (!jwt.getClaim("scp").equals(SCOPE_REFRESH))
            throw new CustomException(INVALID_TOKEN, "refresh token이 아닙니다");

        String subject = jwt.getSubject();
        Long memberId = Long.valueOf(subject);

        if (!memberRepository.existsById(memberId))
            throw new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다");

        String accessToken = jwtProvider.generateAccessToken(subject);
        return new ReissueResponse(accessToken);
    }
}
