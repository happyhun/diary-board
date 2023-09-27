package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findById(Long id);

    void deleteByMemberId(Long memberId);

    Page<Post> findByMemberNicknameContaining(String nickname, Pageable pageable);

    Page<Post> findByTitleContaining(String title, Pageable pageable);

    Page<Post> findByContentContaining(String content, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE "
            + "p.title LIKE %:keyword% OR "
            + "p.content LIKE %:keyword% OR "
            + "p.member.nickname LIKE %:keyword%")
    Page<Post> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
