package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    Member saveMember;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .nickname("임시완")
                .email("test@gmail.com")
                .password("test123!@#")
                .build();

        saveMember = memberRepository.save(member);
    }

    @Test
    void 게시글_생성_단건조회() {
        // given
        String title = "게시판 테스트";
        String content = "게시판테스트입니다.";

        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(saveMember)
                .build();

        // when
        Post savePost = postRepository.save(post);
        Optional<Post> findPost = postRepository.findById(savePost.getId());

        // then
        assertThat(findPost).isNotEmpty();
        assertThat(findPost.get().getTitle()).isEqualTo(post.getTitle());
        assertThat(findPost.get().getContent()).isEqualTo(post.getContent());
        assertThat(findPost.get().getMember().getId()).isEqualTo(saveMember.getId());
    }

    @Test
    void 게시글_생성_삭제() {
        // given
        String title = "게시판 테스트";
        String content = "게시판테스트입니다.";

        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(saveMember)
                .build();

        // when
        Post savePost = postRepository.save(post);
        postRepository.delete(savePost);
        Optional<Post> findPost = postRepository.findById(savePost.getId());

        // then
        assertThat(findPost).isEmpty();
    }

    @Test
    void 게시글_작성자로_조회() {
        // given
        String title = "게시판 테스트";
        String content = "게시판테스트입니다.";
        String keyword = "시완";

        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(saveMember)
                .build();

        Post savePost = postRepository.save(post);

        // when
        Page<Post> postPage = postRepository.findByMemberNicknameContaining(
                keyword,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));

        List<Post> posts = postPage.getContent();

        // then
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void 게시글_전체검색_조회() {
        // given
        String title = "게시판 테스트";
        String content = "게시판테스트입니다.";
        String keyword = "테스트";

        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(saveMember)
                .build();

        Post savePost = postRepository.save(post);

        // when
        Page<Post> postPage = postRepository.findByKeyword(
                keyword,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));

        List<Post> posts = postPage.getContent();

        // then
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getId()).isEqualTo(savePost.getId());
    }
}