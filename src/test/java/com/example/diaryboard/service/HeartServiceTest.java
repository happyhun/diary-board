package com.example.diaryboard.service;

import com.example.diaryboard.dto.heart.CreateHeartRequest;
import com.example.diaryboard.dto.heart.DeleteHeartRequest;
import com.example.diaryboard.entity.HeartType;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.jwt.JwtProvider;
import com.example.diaryboard.global.security.CustomJwtAuthenticationConverter;
import com.example.diaryboard.repository.HeartRepository;
import com.example.diaryboard.repository.MemberRepository;
import com.example.diaryboard.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class HeartServiceTest {

    @Autowired
    HeartService heartService;

    @Autowired
    HeartRepository heartRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    JwtDecoder jwtDecoder;

    CustomJwtAuthenticationConverter jwtAuthenticationConverter = new CustomJwtAuthenticationConverter();

    Long postId;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .nickname("임시완")
                .email("test@gmail.com")
                .password("test123!@#")
                .build();

        Long memberId = memberRepository.save(member).getId();
        String subject = String.valueOf(memberId);
        Jwt accessToken = jwtDecoder.decode(jwtProvider.generateAccessToken(subject));

        Authentication authentication = jwtAuthenticationConverter.convert(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        postId = postRepository.save(post).getId();
    }

    @Test
    void 좋아요_누르기() {
        // given
        CreateHeartRequest dto = new CreateHeartRequest(postId, HeartType.POST);

        // when
        heartService.createHeart(dto);

        // then
        assertThat(heartRepository.findAll()).hasSize(1);
    }

    @Test
    void 좋아요_취소() {
        // given
        CreateHeartRequest createHeartRequest = new CreateHeartRequest(postId, HeartType.POST);
        heartService.createHeart(createHeartRequest);

        // when
        DeleteHeartRequest deleteHeartRequest = new DeleteHeartRequest(postId, HeartType.POST);
        heartService.deleteHeart(deleteHeartRequest);

        // then
        assertThat(heartRepository.findAll()).hasSize(0);
    }
}
