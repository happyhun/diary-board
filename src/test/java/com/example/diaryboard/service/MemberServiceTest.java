package com.example.diaryboard.service;

import com.example.diaryboard.dto.member.*;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.global.security.CustomJwtAuthenticationConverter;
import com.example.diaryboard.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtDecoder jwtDecoder;

    CustomJwtAuthenticationConverter jwtAuthenticationConverter = new CustomJwtAuthenticationConverter();

    @Test
    void 회원가입() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        SignupRequest dto = new SignupRequest(email, password, nickname);

        // when
        Long memberId = memberService.signup(dto);
        Optional<Member> findMember = memberRepository.findById(memberId);

        // then
        assertThat(findMember).isNotEmpty();
        assertThat(findMember.get().getNickname()).isEqualTo(dto.getNickname());
        assertThat(findMember.get().getEmail()).isEqualTo(dto.getEmail());
        assertThat(passwordEncoder.matches(dto.getPassword(), findMember.get().getPassword())).isTrue();
    }

    @Test
    void 중복_회원가입() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        SignupRequest dto1 = new SignupRequest(email, password, nickname);
        SignupRequest dto2 = new SignupRequest(email, password, nickname);

        // when
        memberService.signup(dto1);

        // then
        assertThatThrownBy(() -> memberService.signup(dto2)).isInstanceOf(CustomException.class);
    }

    @Test
    void 로그인() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        SignupRequest dto = new SignupRequest(email, password, nickname);

        // when
        Long memberId = memberService.signup(dto);
        LoginResponse response = memberService.login(new LoginRequest(email, password));

        Jwt accessToken = jwtDecoder.decode(response.getAccessToken());
        Jwt refreshToken = jwtDecoder.decode(response.getRefreshToken());

        // then
        assertThat(accessToken.getSubject()).isEqualTo(String.valueOf(memberId));
        assertThat(refreshToken.getSubject()).isEqualTo(String.valueOf(memberId));
    }

    @Test
    void 엑세스_토큰_재발급() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        SignupRequest dto = new SignupRequest(email, password, nickname);

        // when
        memberService.signup(dto);
        LoginResponse loginResponse = memberService.login(new LoginRequest(email, password));
        Jwt refreshToken = jwtDecoder.decode(loginResponse.getRefreshToken());

        Authentication authentication = jwtAuthenticationConverter.convert(refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ReissueResponse reissueResponse = memberService.reissue();
        Jwt accessToken = jwtDecoder.decode(reissueResponse.getAccessToken());

        // then
        assertThat(refreshToken.getSubject()).isEqualTo(accessToken.getSubject());
    }

    @Test
    void 회원정보_조회() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        SignupRequest dto = new SignupRequest(email, password, nickname);

        // when
        memberService.signup(dto);
        LoginResponse loginResponse = memberService.login(new LoginRequest(email, password));
        Jwt accessToken = jwtDecoder.decode(loginResponse.getRefreshToken());

        Authentication authentication = jwtAuthenticationConverter.convert(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MemberProfileResponse memberProfileResponse = memberService.getMemberProfile();

        // then
        assertThat(memberProfileResponse.getNickname()).isEqualTo(dto.getNickname());
    }

    @Test
    void 회원정보_비밀번호_수정() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";
        String changedPassword = "test123!@#change";

        SignupRequest signupRequest = new SignupRequest(email, password, nickname);

        // when
        Long memberId = memberService.signup(signupRequest);
        LoginResponse loginResponse = memberService.login(new LoginRequest(email, password));
        Jwt accessToken = jwtDecoder.decode(loginResponse.getRefreshToken());

        Authentication authentication = jwtAuthenticationConverter.convert(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        memberService.updateMemberProfile(new MemberProfileRequest(null, changedPassword));
        Optional<Member> member = memberRepository.findById(memberId);

        // then
        assertThat(member.get().getNickname()).isEqualTo(signupRequest.getNickname());
        assertThat(passwordEncoder.matches(changedPassword, member.get().getPassword())).isTrue();
    }

    @Test
    void 회원탈퇴() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        SignupRequest signupRequest = new SignupRequest(email, password, nickname);

        // when
        Long memberId = memberService.signup(signupRequest);
        LoginResponse loginResponse = memberService.login(new LoginRequest(email, password));
        Jwt accessToken = jwtDecoder.decode(loginResponse.getRefreshToken());

        Authentication authentication = jwtAuthenticationConverter.convert(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        memberService.deleteMember();

        // then
        assertThat(memberRepository.existsById(memberId)).isFalse();
    }
}