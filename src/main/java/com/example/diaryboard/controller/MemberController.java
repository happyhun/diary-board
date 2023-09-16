package com.example.diaryboard.controller;

import com.example.diaryboard.dto.*;
import com.example.diaryboard.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public Authentication test() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(@RequestHeader("authorization") String refreshToken){
        refreshToken = refreshToken.replace("Bearer ", "");
        ReissueResponse response = memberService.reissue(refreshToken);

        return ResponseEntity.ok().body(response);
    }
}
