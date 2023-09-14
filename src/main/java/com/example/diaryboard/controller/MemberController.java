package com.example.diaryboard.controller;

import com.example.diaryboard.dto.LoginRequest;
import com.example.diaryboard.dto.LoginResponse;
import com.example.diaryboard.dto.SignupRequest;
import com.example.diaryboard.dto.SignupResponse;
import com.example.diaryboard.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest request) {
        memberService.signup(request);
        SignupResponse response = new SignupResponse("회원가입 성공");

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = memberService.login(request);

        return ResponseEntity.ok().body(response);
    }
}
