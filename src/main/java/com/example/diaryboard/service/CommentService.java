package com.example.diaryboard.service;

import com.example.diaryboard.dto.comment.CreateCommentRequest;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.repository.CommentRepository;
import com.example.diaryboard.repository.MemberRepository;
import com.example.diaryboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.diaryboard.global.exception.ExceptionCode.INVALID_POST;
import static com.example.diaryboard.global.exception.ExceptionCode.INVALID_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Long createComment(Long postId, CreateCommentRequest dto) {
        Long memberId = getMemberIdFromAuthentication();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(INVALID_POST, "존재하지 않는 post id입니다"));

        return commentRepository.save(dto.toEntity(member, post)).getId();
    }

    private Long getMemberIdFromAuthentication() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }


}
