package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Heart;
import com.example.diaryboard.entity.HeartType;
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
class HeartRepositoryTest {

    @Autowired
    HeartRepository heartRepository;

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
    void 게시글_좋아요_생성() {
        // given
        Heart heart = Heart.builder()
                .member(saveMember)
                .post(savePost)
                .type(HeartType.POST)
                .build();

        // when
        Heart saveHeart = heartRepository.save(heart);
        Optional<Heart> findHeart = heartRepository.findById(saveHeart.getId());

        // then
        assertThat(findHeart).isNotEmpty();
        assertThat(findHeart.get().getMember()).isEqualTo(saveMember);
        assertThat(findHeart.get().getPost()).isEqualTo(savePost);
    }

    @Test
    void 게시글_좋아요_삭제() {
        // given
        Heart heart = Heart.builder()
                .member(saveMember)
                .post(savePost)
                .type(HeartType.POST)
                .build();

        Heart saveHeart = heartRepository.save(heart);

        // when
        Heart findHeart = heartRepository.findByMemberIdAndPostId(saveMember.getId(), savePost.getId()).orElseThrow();
        heartRepository.delete(findHeart);

        // then
        assertThat(heartRepository.findById(saveHeart.getId())).isEmpty();
    }
}