package com.example.diaryboard.controller;

import com.example.diaryboard.config.jwt.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collections;

@RestController
public class TestController {

    private final JwtProvider jwtProvider;

    public TestController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/auth/check")
    public Authentication check() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/token")
    public String generateToken() throws Exception {
        return jwtProvider.generateAccessToken(Collections.singletonMap("member_id", "1"), Duration.ofMinutes(5));
    }
}
