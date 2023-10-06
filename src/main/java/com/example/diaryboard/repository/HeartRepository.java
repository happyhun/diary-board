package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByMemberIdAndPostId(Long memberId, Long postId);
}
