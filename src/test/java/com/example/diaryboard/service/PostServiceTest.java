package com.example.diaryboard.service;

import com.example.diaryboard.dto.post.CreatePostRequest;
import com.example.diaryboard.dto.post.GetPostResponse;
import com.example.diaryboard.dto.post.UpdatePostRequest;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.jwt.JwtProvider;
import com.example.diaryboard.global.security.CustomJwtAuthenticationConverter;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    JwtDecoder jwtDecoder;

    CustomJwtAuthenticationConverter jwtAuthenticationConverter = new CustomJwtAuthenticationConverter();

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
    }

    @Test
    void 게시글_생성() {
        // given
        String title = "테스트용 게시글";
        String content = "테스트용 게시글 내용입니다.";

        CreatePostRequest request = new CreatePostRequest(title, content);

        // when
        Long postId = postService.createPost(request);
        Optional<Post> post = postRepository.findById(postId);

        // then
        assertThat(post).isNotEmpty();
        assertThat(post.get().getTitle()).isEqualTo(request.getTitle());
        assertThat(post.get().getContent()).isEqualTo(request.getContent());
    }

    @Test
    void 게시글_삭제() {
        // given
        String title = "테스트용 게시글";
        String content = "테스트용 게시글 내용입니다.";

        CreatePostRequest request = new CreatePostRequest(title, content);
        Long postId = postService.createPost(request);

        // when
        postService.deletePost(postId);
        Optional<Post> post = postRepository.findById(postId);

        // then
        assertThat(post).isEmpty();
    }

    @Test
    void 게시글_단건_조회() {
        // given
        String title = "테스트용 게시글";
        String content = "테스트용 게시글 내용입니다.";

        CreatePostRequest request = new CreatePostRequest(title, content);
        Long postId = postService.createPost(request);

        // when
        GetPostResponse response = postService.getPost(postId);

        // then
        assertThat(response.getId()).isEqualTo(postId);
    }

    @Test
    void 게시글_수정() {
        // given
        String title = "테스트용 게시글";
        String content = "테스트용 게시글 내용입니다.";

        CreatePostRequest request = new CreatePostRequest(title, content);
        Long postId = postService.createPost(request);

        String updatedTitle = "수정된 게시글";

        // when
        postService.updatePost(postId, new UpdatePostRequest(updatedTitle, null));
        Post post = postRepository.findById(postId).orElseThrow();

        // then
        assertThat(post.getTitle()).isEqualTo(updatedTitle);
        assertThat(post.getContent()).isEqualTo(content);
    }

}
