package com.example.diaryboard.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum HeartType {
    POST, COMMENT;

    @JsonCreator // JSON 문자열을 Enum 타입으로 변환
    public static HeartType fromString(String str) {
        return HeartType.valueOf(str.toUpperCase());
    }
}
