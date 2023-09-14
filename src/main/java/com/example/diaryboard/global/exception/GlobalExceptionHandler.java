package com.example.diaryboard.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.diaryboard.global.exception.ExceptionCode.INVALID_JSON_FORMAT;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String detailMessage = e.getFieldError().getDefaultMessage();
        if (detailMessage == null)
            detailMessage = "";

        CustomException ce = new CustomException(INVALID_JSON_FORMAT, detailMessage);
        ErrorResponse response = new ErrorResponse(ce.getExceptionCode().getCode(), ce.getMessage(), ce.getDetailMessage());
        return ResponseEntity.status(ce.getExceptionCode().getStatus()).body(response);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse response = new ErrorResponse(e.getExceptionCode().getCode(), e.getMessage(), e.getDetailMessage());
        return ResponseEntity.status(e.getExceptionCode().getStatus()).body(response);
    }
}
