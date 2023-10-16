package com.example.diaryboard.dto.comment;

import com.example.diaryboard.entity.Comment;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCommentRequest {

    @NotNull
    private Long postId;

    @NotBlank
    private String content;

    public Comment toEntity(Member member, Post post) {
        return Comment.builder()
                .content(content)
                .member(member)
                .post(post)
                .build();
    }
}
