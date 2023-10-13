package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = "member")
    Optional<Post> findById(Long id);

    void deleteByMemberId(Long memberId);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByMemberNicknameContainingAndCreatedAtBetween(String nickname, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByTitleContainingAndCreatedAtBetween(String title, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByContentContainingAndCreatedAtBetween(String content, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    @Query("SELECT p FROM Post p WHERE "
            + "(p.title LIKE %:keyword% OR "
            + "p.content LIKE %:keyword% OR "
            + "p.member.nickname LIKE %:keyword%) AND "
            + "(p.createdAt BETWEEN :startDate AND :endDate)")
    Page<Post> findByKeywordAndCreatedAtBetween(@Param("keyword") String keyword,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                Pageable pageable);
}
