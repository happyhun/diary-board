package com.example.diaryboard.dto.comment;

import com.example.diaryboard.entity.Comment;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateCommentRequest {

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
