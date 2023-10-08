package com.example.diaryboard.service;

import com.example.diaryboard.dto.heart.CreateHeartRequest;
import com.example.diaryboard.dto.heart.DeleteHeartRequest;
import com.example.diaryboard.entity.Heart;
import com.example.diaryboard.entity.HeartType;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.exception.CustomException;
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

    public void createHeart(CreateHeartRequest dto) {
        Long memberId = getMemberIdFromAuthentication();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다"));

        if (dto.getHeartType() == HeartType.POST) {
            Post post = postRepository.findById(dto.getId())
                    .orElseThrow(() -> new CustomException(INVALID_POST, "존재하지 않는 post id입니다"));

            post.updateHeartCount(post.getHeartCount() + 1);
            heartRepository.save(dto.toEntity(member, post));
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
    }
}
