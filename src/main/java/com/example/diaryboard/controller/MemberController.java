package com.example.diaryboard.controller;

import com.example.diaryboard.dto.BasicMessageResponse;
import com.example.diaryboard.dto.member.*;
import com.example.diaryboard.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<BasicMessageResponse> signup(@RequestBody @Valid SignupRequest request) {
        memberService.signup(request);
        BasicMessageResponse response = new BasicMessageResponse("회원가입 성공");

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = memberService.login(request);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue() {
        ReissueResponse response = memberService.reissue();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<MemberProfileResponse> getMemberProfile() {
        MemberProfileResponse response = memberService.getMemberProfile();

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping
    public ResponseEntity<BasicMessageResponse> updateMemberProfile(@RequestBody @Valid MemberProfileRequest request) {
        memberService.updateMemberProfile(request);
        BasicMessageResponse response = new BasicMessageResponse("회원정보 수정 성공");

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<BasicMessageResponse> deleteMember() {
        memberService.deleteMember();
        BasicMessageResponse response = new BasicMessageResponse("회원탈퇴 성공");

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/test")
    public Authentication test() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
