package com.example.diaryboard.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    CONSTRAINT_VIOLATION(BAD_REQUEST, "DB_001", "DB 제약 조건 위반입니다"),
    INVALID_JSON_FORMAT(BAD_REQUEST, "JSON_001", "잘못된 JSON 요청입니다"),
    DUPLICATED_EMAIL(CONFLICT, "MEMBER_001", "중복된 이메일입니다"),
    DUPLICATED_NICKNAME(CONFLICT, "MEMBER_002", "중복된 닉네임입니다"),
    UNAUTHORIZED_LOGIN(UNAUTHORIZED, "MEMBER_003", "잘못된 로그인 요청입니다"),
    INVALID_TOKEN(BAD_REQUEST, "TOKEN_001", "유효하지 않은 토큰입니다"),
    UNAUTHORIZED_TOKEN(UNAUTHORIZED, "TOKEN_002", "권한이 없는 토큰입니다"),
    INVALID_POST(BAD_REQUEST, "POST_001", "존재하지 않는 게시글입니다"),
    UNAUTHORIZED_POST(UNAUTHORIZED, "POST_002", "잘못된 게시글 요청입니다"),
    INVALID_COMMENT(BAD_REQUEST, "COMMENT_001", "존재하지 않는 댓글입니다"),
    UNAUTHORIZED_COMMENT(UNAUTHORIZED, "COMMENT_002", "잘못된 댓글 요청입니다"),
    INVALID_HEART(BAD_REQUEST, "HEART_001", "존재하지 않는 좋아요입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
