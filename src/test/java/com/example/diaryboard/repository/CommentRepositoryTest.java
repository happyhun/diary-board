package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Comment;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    Member saveMember;
    Post savePost;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .nickname("임시완")
                .email("test@gmail.com")
                .password("test123!@#")
                .build();

        saveMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("게시판 테스트")
                .content("게시판테스트입니다.")
                .image("이미지")
                .member(saveMember)
                .build();

        savePost = postRepository.save(post);
    }

    @Test
    void 댓글_생성_조회() {
        // given
        String content = "댓글 테스트";
        Comment comment = Comment.builder()
                .content(content)
                .member(saveMember)
                .post(savePost)
                .build();

        // when
        Comment saveComment = commentRepository.save(comment);
        Optional<Comment> findComment = commentRepository.findById(saveComment.getId());

        // then
        assertThat(findComment).isNotEmpty();
        assertThat(findComment.get().getContent()).isEqualTo(content);
        assertThat(findComment.get().getMember()).isEqualTo(saveMember);
        assertThat(findComment.get().getPost()).isEqualTo(savePost);
    }

    @Test
    void 댓글_생성_삭제() {
        // given
        String content = "댓글 테스트";
        Comment comment = Comment.builder()
                .content(content)
                .member(saveMember)
                .post(savePost)
                .build();

        // when
        Comment saveComment = commentRepository.save(comment);
        commentRepository.delete(saveComment);
        Optional<Comment> findComment = commentRepository.findById(saveComment.getId());

        // then
        assertThat(findComment).isEmpty();
    }
}