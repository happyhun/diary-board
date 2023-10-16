package com.example.diaryboard.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 엔터티 클래스가 상속할 수 있도록 만들며, 필드들은 컬럼으로 사용된다.
@EntityListeners(AuditingEntityListener.class) // 생성 시간, 수정 시간 등의 정보를 자동으로 채워주는 기능 제공
abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
