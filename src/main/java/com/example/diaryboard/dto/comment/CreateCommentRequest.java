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
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 프록시 객체를 만들 때만 사용하도록, 기본 생성자를 protected로 설정
@AllArgsConstructor // 테스트를 위해 생성자를 추가
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) // JSON의 snake_case 값을 Java의 camelCase 값으로 매핑
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
