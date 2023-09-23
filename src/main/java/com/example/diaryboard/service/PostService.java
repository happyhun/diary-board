package com.example.diaryboard.service;

import com.example.diaryboard.dto.post.CreatePostRequest;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.repository.MemberRepository;
import com.example.diaryboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.diaryboard.global.exception.ExceptionCode.INVALID_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Long createPost(CreatePostRequest dto) {
        Long memberId = getMemberIdFromAuthentication();
        Member member = validateMemberId(memberId);

        Post post = dto.toEntity(member);

        return postRepository.save(post).getId();
    }

    private Long getMemberIdFromAuthentication() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private Member validateMemberId(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isEmpty())
            throw new CustomException(INVALID_TOKEN, "존재하지 않는 subject입니다");

        return member.get();
    }
}
