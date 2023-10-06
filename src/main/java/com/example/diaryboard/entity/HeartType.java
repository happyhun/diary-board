package com.example.diaryboard.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum HeartType {
    POST, COMMENT;

    @JsonCreator
    public static HeartType forValue(String value) {
        return HeartType.valueOf(value.toUpperCase());
    }
}
