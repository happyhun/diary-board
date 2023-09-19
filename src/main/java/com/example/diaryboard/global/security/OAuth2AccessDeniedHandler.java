package com.example.diaryboard.global.security;

import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static com.example.diaryboard.global.exception.ExceptionCode.INVALID_TOKEN;

public class OAuth2AccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        CustomException e = new CustomException(INVALID_TOKEN, accessDeniedException.getMessage());
        writeErrorResponse(response, e);
    }

    private void writeErrorResponse(HttpServletResponse response, CustomException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(e.getExceptionCode().getStatus().value());

        ErrorResponse errorResponse = new ErrorResponse(e.getExceptionCode().getCode(), e.getMessage(), e.getDetailMessage());

        String result = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(result);
    }
}
