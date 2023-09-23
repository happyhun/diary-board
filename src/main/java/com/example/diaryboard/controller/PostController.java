package com.example.diaryboard.controller;

import com.example.diaryboard.dto.BasicMessageResponse;
import com.example.diaryboard.dto.post.CreatePostRequest;
import com.example.diaryboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<BasicMessageResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        postService.createPost(request);
        BasicMessageResponse response = new BasicMessageResponse("게시글 생성 성공");

        return ResponseEntity.ok().body(response);
    }
}
