package com.example.diaryboard.dto.post;

import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatePostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private String image;

    public Post toEntity(Member member) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .image(image)
                .build();
    }
}
