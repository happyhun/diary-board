package com.example.diaryboard.global.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.diaryboard.global.exception.ExceptionCode.CONSTRAINT_VIOLATION;
import static com.example.diaryboard.global.exception.ExceptionCode.INVALID_JSON_FORMAT;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse response = new ErrorResponse(e.getExceptionCode().getCode(), e.getMessage(), e.getDetailMessage());
        return ResponseEntity.status(e.getExceptionCode().getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(", ");
        }

        String detailMessage = sb.toString();

        CustomException ce = new CustomException(INVALID_JSON_FORMAT, detailMessage);
        ErrorResponse response = new ErrorResponse(ce.getExceptionCode().getCode(), ce.getMessage(), ce.getDetailMessage());
        return ResponseEntity.status(ce.getExceptionCode().getStatus()).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String detailMessage = e.getMessage();

        CustomException ce = new CustomException(CONSTRAINT_VIOLATION, detailMessage);
        ErrorResponse response = new ErrorResponse(ce.getExceptionCode().getCode(), ce.getMessage(), ce.getDetailMessage());
        return ResponseEntity.status(ce.getExceptionCode().getStatus()).body(response);
    }
}
