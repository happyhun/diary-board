package com.example.diaryboard.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    INVALID_JSON_FORMAT(BAD_REQUEST, "JSON_001", "잘못된 JSON 요청입니다"),
    DUPLICATED_EMAIL(CONFLICT, "MEMBER_001", "중복된 이메일입니다"),
    DUPLICATED_NICKNAME(CONFLICT, "MEMBER_002", "중복된 닉네임입니다"),
    UNAUTHORIZED_LOGIN(UNAUTHORIZED, "MEMBER_003", "잘못된 로그인 요청입니다"),
    INVALID_TOKEN(UNAUTHORIZED, "TOKEN_001", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(UNAUTHORIZED, "TOKEN_002", "만료된 토큰입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
