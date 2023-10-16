package com.example.diaryboard.service;

import com.example.diaryboard.dto.comment.CreateCommentRequest;
import com.example.diaryboard.dto.comment.UpdateCommentRequest;
import com.example.diaryboard.entity.Comment;
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

import static com.example.diaryboard.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional // public 메소드에만 적용됨
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Long createComment(CreateCommentRequest dto) {
        Long memberId = getMemberIdFromAuthentication();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new CustomException(INVALID_POST, "존재하지 않는 post id입니다"));

        return commentRepository.save(dto.toEntity(member, post)).getId();
    }

    private Long getMemberIdFromAuthentication() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(INVALID_COMMENT, "존재하지 않는 comment id입니다"));

        Long memberId = getMemberIdFromAuthentication();
        Long commentMemberId = comment.getMember().getId();

        if (!commentMemberId.equals(memberId))
            throw new CustomException(UNAUTHORIZED_COMMENT, "삭제 권한이 없습니다");

        commentRepository.deleteById(commentId);
    }

    public void updateComment(Long commentId, UpdateCommentRequest dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(INVALID_COMMENT, "존재하지 않는 comment id입니다"));

        Long memberId = getMemberIdFromAuthentication();
        Long commentMemberId = comment.getMember().getId();

        if (!commentMemberId.equals(memberId))
            throw new CustomException(UNAUTHORIZED_COMMENT, "수정 권한이 없습니다");

        comment.updateContent(dto.getContent());
    }
}
