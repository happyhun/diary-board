package com.example.diaryboard.controller;

import com.example.diaryboard.dto.BasicMessageResponse;
import com.example.diaryboard.dto.post.CreatePostRequest;
import com.example.diaryboard.dto.post.GetPostResponse;
import com.example.diaryboard.dto.post.UpdatePostRequest;
import com.example.diaryboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<BasicMessageResponse> createPost(@RequestBody @Valid CreatePostRequest request) {
        postService.createPost(request);
        BasicMessageResponse response = new BasicMessageResponse("게시글 생성 성공");

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BasicMessageResponse> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        BasicMessageResponse response = new BasicMessageResponse("게시글 삭제 성공");

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable Long postId) {
        GetPostResponse response = postService.getPost(postId);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<BasicMessageResponse> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        postService.updatePost(postId, request);
        BasicMessageResponse response = new BasicMessageResponse("게시글 수정 성공");

        return ResponseEntity.ok().body(response);
    }
}
