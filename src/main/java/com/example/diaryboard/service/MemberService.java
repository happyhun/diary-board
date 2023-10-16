package com.example.diaryboard.service;

import com.example.diaryboard.dto.member.*;
import com.example.diaryboard.entity.Heart;
import com.example.diaryboard.entity.HeartType;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.global.jwt.JwtProvider;
import com.example.diaryboard.repository.HeartRepository;
import com.example.diaryboard.repository.MemberRepository;
import com.example.diaryboard.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.diaryboard.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final PostRepository postRepository;
    private final HeartRepository heartRepository;
    private ModelMapper modelMapper;

    @PostConstruct
    protected void init() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Long signup(SignupRequest dto) {
        validateSignup(dto);
        Member member = dto.toEntity(passwordEncoder);

        return memberRepository.save(member).getId();
    }

    private void validateSignup(SignupRequest dto) {
        if (memberRepository.existsByEmail(dto.getEmail()))
            throw new CustomException(DUPLICATED_EMAIL, "이미 가입된 이메일입니다");

        if (memberRepository.existsByNickname(dto.getNickname()))
            throw new CustomException(DUPLICATED_NICKNAME, "이미 가입된 닉네임입니다");
    }

    public LoginResponse login(LoginRequest dto) {
        Long memberId = validateLogin(dto);

        String subject = String.valueOf(memberId);
        String accessToken = jwtProvider.generateAccessToken(subject);
        String refreshToken = jwtProvider.generateRefreshToken(subject);

        return new LoginResponse(accessToken, refreshToken);
    }

    private Long validateLogin(LoginRequest dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(UNAUTHORIZED_LOGIN, "가입되지 않은 이메일입니다"));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword()))
            throw new CustomException(UNAUTHORIZED_LOGIN, "틀린 비밀번호입니다");

        return member.getId();
    }

    public ReissueResponse reissue() {
        Long memberId = getMemberIdFromAuthentication();

        if (!memberRepository.existsById(memberId))
            throw new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다");

        String subject = String.valueOf(memberId);
        String accessToken = jwtProvider.generateAccessToken(subject);

        return new ReissueResponse(accessToken);
    }

    private Long getMemberIdFromAuthentication() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public MemberProfileResponse getMemberProfile() {
        Long memberId = getMemberIdFromAuthentication();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다"));

        return new MemberProfileResponse(member.getNickname());
    }

    public void updateMemberProfile(MemberProfileRequest request) {
        validateUpdateMemberProfile(request);

        Long memberId = getMemberIdFromAuthentication();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다"));

        modelMapper.map(request, member);
    }

    private void validateUpdateMemberProfile(MemberProfileRequest request) {
        if (StringUtils.hasLength(request.getNickname()) && memberRepository.existsByNickname(request.getNickname()))
            throw new CustomException(DUPLICATED_NICKNAME, "사용중인 닉네임입니다");

        if (StringUtils.hasLength(request.getPassword()))
            request.encodePassword(passwordEncoder);
    }

    public void deleteMember() {
        Long memberId = getMemberIdFromAuthentication();
        postRepository.deleteByMemberId(memberId);
        List<Heart> hearts = heartRepository.findByMemberId(memberId);
        hearts.forEach(heart -> {
            if (heart.getType() == HeartType.POST) {
                heart.getPost().updateHeartCount(heart.getPost().getHeartCount() - 1);
            } else {
                heart.getComment().updateHeartCount(heart.getComment().getHeartCount() - 1);
            }
        });
        heartRepository.deleteAll(hearts);
        memberRepository.deleteById(memberId);
    }
}
