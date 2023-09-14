package com.example.diaryboard.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;
    private final String detailMessage;

    public CustomException(ExceptionCode exceptionCode, String detailMessage) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.detailMessage = detailMessage;
    }
}
