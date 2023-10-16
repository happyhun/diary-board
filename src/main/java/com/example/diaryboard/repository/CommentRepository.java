package com.example.diaryboard.repository;

import com.example.diaryboard.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"member"}) // member 필드를 left outer join으로 가져옴
    Optional<Comment> findById(Long id);
}
