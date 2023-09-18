package com.example.diaryboard.controller;

import com.example.diaryboard.dto.*;
import com.example.diaryboard.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ReissueResponse> reissue(@RequestHeader("authorization") String refreshToken) {
        refreshToken = refreshToken.replace("Bearer ", "");
        ReissueResponse response = memberService.reissue(refreshToken);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<MemberProfileResponse> getMemberProfile(@RequestHeader("authorization") String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");
        MemberProfileResponse response = memberService.getMemberProfile(accessToken);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<BasicMessageResponse> changeNickname(@RequestHeader("authorization") String accessToken,
                                                                @RequestBody ChangeNicknameRequest request) {
        accessToken = accessToken.replace("Bearer ", "");
        memberService.changeNickname(accessToken, request);
        BasicMessageResponse response = new BasicMessageResponse("닉네임 변경 성공");

        return ResponseEntity.ok().body(response);
    }
}
