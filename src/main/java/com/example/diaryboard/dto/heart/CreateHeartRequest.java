package com.example.diaryboard.dto.heart;

import com.example.diaryboard.entity.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateHeartRequest {

    @NotNull
    private Long id;

    @NotNull
    private HeartType heartType;

    public Heart toEntity(Member member, Post post) {
        return Heart.builder()
                .member(member)
                .post(post)
                .type(heartType)
                .build();
    }

    public Heart toEntity(Member member, Comment comment) {
        return Heart.builder()
                .member(member)
                .comment(comment)
                .type(heartType)
                .build();
    }
}
