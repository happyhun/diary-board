package com.example.diaryboard.service;

import com.example.diaryboard.dto.SignupRequest;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

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
        assertThatThrownBy(() -> memberService.signup(dto2)).isInstanceOf(RuntimeException.class);
    }
}