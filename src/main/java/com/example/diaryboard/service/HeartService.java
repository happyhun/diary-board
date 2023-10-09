package com.example.diaryboard.service;

import com.example.diaryboard.dto.heart.CreateHeartRequest;
import com.example.diaryboard.dto.heart.DeleteHeartRequest;
import com.example.diaryboard.entity.*;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.repository.CommentRepository;
import com.example.diaryboard.repository.HeartRepository;
import com.example.diaryboard.repository.MemberRepository;
import com.example.diaryboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.diaryboard.global.exception.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void createHeart(CreateHeartRequest dto) {
        Long memberId = getMemberIdFromAuthentication();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다"));

        if (dto.getHeartType() == HeartType.POST) {
            Post post = postRepository.findById(dto.getId())
                    .orElseThrow(() -> new CustomException(INVALID_POST, "존재하지 않는 post id입니다"));

            if (heartRepository.existsByMemberIdAndPostId(memberId, dto.getId()))
                throw new CustomException(DUPLICATED_HEART, "이미 좋아요를 누른 게시글입니다");

            post.updateHeartCount(post.getHeartCount() + 1);
            heartRepository.save(dto.toEntity(member, post));
        }

        if (dto.getHeartType() == HeartType.COMMENT) {
            Comment comment = commentRepository.findById(dto.getId())
                    .orElseThrow(() -> new CustomException(INVALID_COMMENT, "존재하지 않는 comment id입니다"));

            if (heartRepository.existsByMemberIdAndCommentId(memberId, dto.getId()))
                throw new CustomException(DUPLICATED_HEART, "이미 좋아요를 누른 댓글입니다");

            comment.updateHeartCount(comment.getHeartCount() + 1);
            heartRepository.save(dto.toEntity(member, comment));
        }
    }

    private Long getMemberIdFromAuthentication() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void deleteHeart(DeleteHeartRequest dto) {
        Long memberId = getMemberIdFromAuthentication();

        if (dto.getHeartType() == HeartType.POST) {
            Heart heart = heartRepository.findByMemberIdAndPostId(memberId, dto.getId())
                    .orElseThrow(() -> new CustomException(INVALID_HEART, "게시글에 대한 좋아요가 없습니다"));

            Post post = heart.getPost();
            post.updateHeartCount(post.getHeartCount() - 1);
            heartRepository.delete(heart);
        }

        if (dto.getHeartType() == HeartType.COMMENT) {
            Heart heart = heartRepository.findByMemberIdAndCommentId(memberId, dto.getId())
                    .orElseThrow(() -> new CustomException(INVALID_HEART, "댓글에 대한 좋아요가 없습니다"));

            Comment comment = heart.getComment();
            comment.updateHeartCount(comment.getHeartCount() - 1);
            heartRepository.delete(heart);
        }
    }
}
