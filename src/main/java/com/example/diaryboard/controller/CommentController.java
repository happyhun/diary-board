package com.example.diaryboard.controller;

import com.example.diaryboard.dto.BasicMessageResponse;
import com.example.diaryboard.dto.comment.CreateCommentRequest;
import com.example.diaryboard.dto.comment.UpdateCommentRequest;
import com.example.diaryboard.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // @Controller + @ResponseBody (객체를 JSON 형태로 반환)
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자로 가지는 생성자를 생성
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<BasicMessageResponse> createComment(@RequestBody @Valid CreateCommentRequest request) {
        commentService.createComment(request);
        BasicMessageResponse response = new BasicMessageResponse("댓글 생성 성공");

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{commentId}") // URI 템플릿 변수를 @PathVariable 변수에 할당
    public ResponseEntity<BasicMessageResponse> updateComment(@PathVariable Long commentId, @RequestBody @Valid UpdateCommentRequest request) {
        commentService.updateComment(commentId, request);
        BasicMessageResponse response = new BasicMessageResponse("댓글 수정 성공");

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<BasicMessageResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        BasicMessageResponse response = new BasicMessageResponse("댓글 삭제 성공");

        return ResponseEntity.ok().body(response);
    }
}
