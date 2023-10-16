package com.example.diaryboard.service;

import com.example.diaryboard.dto.comment.GetCommentResponse;
import com.example.diaryboard.dto.post.*;
import com.example.diaryboard.entity.Comment;
import com.example.diaryboard.entity.Heart;
import com.example.diaryboard.entity.Member;
import com.example.diaryboard.entity.Post;
import com.example.diaryboard.global.exception.CustomException;
import com.example.diaryboard.repository.HeartRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.diaryboard.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final HeartRepository heartRepository;
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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (name.equals("anonymousUser"))
            return 0L;

        return Long.valueOf(name);
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

        Long memberId = getMemberIdFromAuthentication();

        List<Long> commentIds = post.getComments().stream()
                .map(Comment::getId)
                .toList();

        Set<Long> likedCommentIds = heartRepository.findByMemberIdAndCommentIdIn(memberId, commentIds).stream()
                .map(Heart::getComment)
                .map(Comment::getId)
                .collect(Collectors.toSet());

        List<GetCommentResponse> comments = post.getComments().stream()
                .map(comment -> new GetCommentResponse(comment, likedCommentIds.contains(comment.getId())))
                .toList();

        return new GetPostResponse(post, comments, heartRepository.existsByMemberIdAndPostId(memberId, postId));
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

    public Page<GetPostPageResponse> getPostPage(GetPostPageRequestParam param) {
        PageRequest pageRequest = createPageRequest(param.getPage(), param.getSize(), param.getSortBy(), param.getDirection());

        if (param.getKeyword().isEmpty()) {
            return postRepository.findByCreatedAtBetween(param.getStartDate().atStartOfDay(), param.getEndDate().atTime(23, 59, 59), pageRequest).map(GetPostPageResponse::new);
        }

        return switch (param.getSearchBy()) {
            case ALL ->
                    postRepository.findByKeywordAndCreatedAtBetween(param.getKeyword(), param.getStartDate().atStartOfDay(), param.getEndDate().atTime(23, 59, 59), pageRequest).map(GetPostPageResponse::new);
            case AUTHOR ->
                    postRepository.findByMemberNicknameContainingAndCreatedAtBetween(param.getKeyword(), param.getStartDate().atStartOfDay(), param.getEndDate().atTime(23, 59, 59), pageRequest).map(GetPostPageResponse::new);
            case TITLE ->
                    postRepository.findByTitleContainingAndCreatedAtBetween(param.getKeyword(), param.getStartDate().atStartOfDay(), param.getEndDate().atTime(23, 59, 59), pageRequest).map(GetPostPageResponse::new);
            case CONTENT ->
                    postRepository.findByContentContainingAndCreatedAtBetween(param.getKeyword(), param.getStartDate().atStartOfDay(), param.getEndDate().atTime(23, 59, 59), pageRequest).map(GetPostPageResponse::new);
        };
    }

    private PageRequest createPageRequest(int page, int size, SortType sortBy, DirectionType direction) {
        String sortName = sortBy.name().equalsIgnoreCase("HEART") ? "heartCount" : sortBy.name().toLowerCase();
        Sort sort = (direction == DirectionType.ASC) ? Sort.by(sortName).ascending() : Sort.by(sortName).descending();
        return PageRequest.of(page, size, sort);
    }
}
