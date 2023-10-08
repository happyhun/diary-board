package com.example.diaryboard.dto.comment;

import com.example.diaryboard.entity.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetCommentResponse {

    private final Long commentId;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Integer heartCount;

    public GetCommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getMember().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.heartCount = comment.getHeartCount();
    }
}
