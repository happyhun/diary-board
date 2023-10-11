package com.example.diaryboard.dto.post;

import com.example.diaryboard.dto.comment.GetCommentResponse;
import com.example.diaryboard.entity.Post;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetPostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Integer heartCount;
    private final List<GetCommentResponse> comments;
    private final Boolean isHearted;

    public GetPostResponse(Post post, List<GetCommentResponse> comments, Boolean isHearted) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getMember().getNickname();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.heartCount = post.getHeartCount();
        this.comments = comments;
        this.isHearted = isHearted;
    }
}
