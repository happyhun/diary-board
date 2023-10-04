package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"member", "comments.member"})
    Optional<Post> findById(Long id);

    void deleteByMemberId(Long memberId);

    @EntityGraph(attributePaths = "member")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByMemberNicknameContaining(String nickname, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByTitleContaining(String title, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByContentContaining(String content, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    @Query("SELECT p FROM Post p WHERE "
            + "p.title LIKE %:keyword% OR "
            + "p.content LIKE %:keyword% OR "
            + "p.member.nickname LIKE %:keyword%")
    Page<Post> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
