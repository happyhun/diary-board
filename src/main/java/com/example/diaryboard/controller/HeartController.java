package com.example.diaryboard.controller;

import com.example.diaryboard.dto.BasicMessageResponse;
import com.example.diaryboard.dto.heart.CreateHeartRequest;
import com.example.diaryboard.dto.heart.DeleteHeartRequest;
import com.example.diaryboard.service.HeartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hearts")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping
    public ResponseEntity<BasicMessageResponse> createHeart(@RequestBody @Valid CreateHeartRequest request) {
        heartService.createHeart(request);
        BasicMessageResponse response = new BasicMessageResponse("좋아요 생성 성공");

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<BasicMessageResponse> deleteHeart(@RequestBody @Valid DeleteHeartRequest request) {
        heartService.deleteHeart(request);
        BasicMessageResponse response = new BasicMessageResponse("좋아요 삭제 성공");

        return ResponseEntity.ok().body(response);
    }
}
