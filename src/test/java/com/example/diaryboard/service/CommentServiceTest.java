package com.example.diaryboard.service;

import com.example.diaryboard.dto.comment.CreateCommentRequest;
import com.example.diaryboard.dto.comment.UpdateCommentRequest;
import com.example.diaryboard.entity.Comment;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.jwt.JwtProvider;
import com.example.diaryboard.global.security.CustomJwtAuthenticationConverter;
import com.example.diaryboard.repository.CommentRepository;
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
class CommentServiceTest {

    @Autowired
    CommentService commentService;
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
    @Autowired
    private CommentRepository commentRepository;

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
    void 댓글_생성() {
        // given
        String content = "댓글 내용입니다.";
        CreateCommentRequest request = new CreateCommentRequest(content);

        // when
        Long commentId = commentService.createComment(postId, request);
        Optional<Comment> comment = commentRepository.findById(commentId);

        // then
        assertThat(comment).isNotEmpty();
        assertThat(comment.get().getContent()).isEqualTo(request.getContent());
    }

    @Test
    void 댓글_삭제() {
        // given
        String content = "댓글 내용입니다.";

        CreateCommentRequest request = new CreateCommentRequest(content);
        Long commentId = commentService.createComment(postId, request);

        // when
        commentService.deleteComment(commentId);
        Optional<Comment> comment = commentRepository.findById(commentId);

        // then
        assertThat(comment).isEmpty();
    }

    @Test
    void 댓글_수정() {
        // given
        String content = "댓글 내용입니다.";

        CreateCommentRequest request = new CreateCommentRequest(content);
        Long commentId = commentService.createComment(postId, request);

        String updatedContent = "수정된 댓글 내용입니다.";

        // when
        commentService.updateComment(commentId, new UpdateCommentRequest(updatedContent));
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // then
        assertThat(comment.getContent()).isEqualTo(updatedContent);
    }
}