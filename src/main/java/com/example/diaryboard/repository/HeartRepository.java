package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Heart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    @EntityGraph(attributePaths = {"member", "post"})
    Optional<Heart> findByMemberIdAndPostId(Long memberId, Long postId);

    @EntityGraph(attributePaths = {"member", "comment"})
    Optional<Heart> findByMemberIdAndCommentId(Long memberId, Long commentId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    List<Heart> findByMemberId(Long memberId);

    List<Heart> findByMemberIdAndCommentIdIn(Long memberId, List<Long> commentIds);
}
