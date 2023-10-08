package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Heart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    @EntityGraph(attributePaths = {"member", "post"})
    Optional<Heart> findByMemberIdAndPostId(Long memberId, Long postId);
}
