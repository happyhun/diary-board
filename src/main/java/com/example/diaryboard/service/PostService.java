package com.example.diaryboard.service;

import com.example.diaryboard.dto.post.*;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.repository.MemberRepository;
import com.example.diaryboard.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private ModelMapper modelMapper;

    @PostConstruct
    protected void init() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

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

    public void updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(INVALID_POST, "존재하지 않는 post id입니다"));

        Long memberId = getMemberIdFromAuthentication();
        Member member = post.getMember();

        if (!member.getId().equals(memberId))
            throw new CustomException(UNAUTHORIZED_POST, "수정 권한이 없습니다");

        modelMapper.map(request, post);
    }

    public Page<GetPostResponse> getPosts(int page, int size, SortType sortBy, DirectionType direction, SearchType searchBy, String keyword) {
        PageRequest pageRequest = createPageRequest(page, size, sortBy, direction);

        if (keyword.isEmpty()) {
            return postRepository.findAll(pageRequest).map(GetPostResponse::new);
        }

        return switch (searchBy) {
            case ALL -> postRepository.findByKeyword(keyword, pageRequest).map(GetPostResponse::new);
            case AUTHOR -> postRepository.findByMemberNicknameContaining(keyword, pageRequest).map(GetPostResponse::new);
            case TITLE -> postRepository.findByTitleContaining(keyword, pageRequest).map(GetPostResponse::new);
            case CONTENT -> postRepository.findByContentContaining(keyword, pageRequest).map(GetPostResponse::new);
        };
    }

    private PageRequest createPageRequest(int page, int size, SortType sortBy, DirectionType direction) {
        Sort sort = (direction == DirectionType.ASC) ? Sort.by(sortBy.name().toLowerCase()).ascending() : Sort.by(sortBy.name().toLowerCase()).descending();
        return PageRequest.of(page, size, sort);
    }
}
