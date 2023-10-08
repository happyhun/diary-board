package com.example.diaryboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @Builder.Default
    private Integer heartCount = 0;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateHeartCount(Integer heartCount) {
        this.heartCount = heartCount;
    }
}
