package com.example.diaryboard.service;

import com.example.diaryboard.dto.post.CreatePostRequest;
import com.example.diaryboard.dto.post.GetPostResponse;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.repository.MemberRepository;
import com.example.diaryboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.diaryboard.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Long createPost(CreatePostRequest dto) {
        Long memberId = getMemberIdFromAuthentication();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다"));

        Post post = dto.toEntity(member);

        return postRepository.save(post).getId();
    }

    private Long getMemberIdFromAuthentication() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(INVALID_POST, "존재하지 않는 post id입니다"));

        Long memberId = getMemberIdFromAuthentication();
        Member member = post.getMember();

        if (!member.getId().equals(memberId))
            throw new CustomException(UNAUTHORIZED_POST, "삭제 권한이 없습니다");

        postRepository.deleteById(postId);
    }

    public GetPostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(INVALID_POST, "존재하지 않는 post id입니다"));

        return new GetPostResponse(post);
    }
}
