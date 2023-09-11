package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원_생성_조회() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();

        // when
        Member saveMember = memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findById(saveMember.getId());

        // then
        assertThat(findMember).isNotEmpty();
        assertThat(findMember.get().getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.get().getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    void 회원_생성_삭제() {
        // given
        String nickname = "임시완";
        String email = "test@gmail.com";
        String password = "test123!@#";

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();

        // when
        Member saveMember = memberRepository.save(member);
        memberRepository.delete(saveMember);
        Optional<Member> findMember = memberRepository.findById(saveMember.getId());

        // then
        assertThat(findMember).isEmpty();
    }
}