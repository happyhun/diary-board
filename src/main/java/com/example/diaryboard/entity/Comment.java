package com.example.diaryboard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue // 기본 값은 AUTO
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @Builder.Default
    @Column(nullable = false)
    private Integer heartCount = 0;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateHeartCount(Integer heartCount) {
        this.heartCount = heartCount;
    }

}
