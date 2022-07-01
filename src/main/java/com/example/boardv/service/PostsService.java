package com.example.boardv.service;


import com.example.boardv.domain.Posts;
import com.example.boardv.dto.PostListResponseDto;
import com.example.boardv.dto.PostsResponseDto;
import com.example.boardv.dto.PostsSaveRequestDto;
import com.example.boardv.domain.PostsRepository;
import com.example.boardv.dto.PostsUpdateRequestDto;
import com.example.boardv.domain.web.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postRepository;


    //id에 따른 게시글 조회
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id=" +id));

        return new PostsResponseDto(entity);
    }

    //검색에 따른 게시글 조회
    @Transactional(readOnly = true)
    public Page<PostListResponseDto> search(SearchType searchType, String keyword, Pageable pageable) {
        if (searchType == SearchType.TITLE) {
            return postRepository.findByTitleContaining(keyword, pageable)
                    .map(PostListResponseDto::new);
        } else if (searchType == SearchType.AUTHOR) {
            return postRepository.findByAuthorContaining(keyword, pageable)
                    .map(PostListResponseDto::new);
        }
        return postRepository.findAll(pageable).map(PostListResponseDto::new);
    }

    //다음 페이지 확인 여부
    @Transactional(readOnly = true)
    public Boolean getNextPageCheck(SearchType searchType, String keyword, Pageable pageable){
        if(keyword != null) {
            Page<PostListResponseDto> saved = search(searchType, keyword, pageable);
            Boolean check = saved.hasNext(); // 다음 페이지가 있는지 확인 있으면 True, 없으면 False
            return check;
        }
        Page<PostListResponseDto> saved = search(SearchType.ALL,null, pageable);
        Boolean check = saved.hasNext();
        return check;
    }
    @Transactional(readOnly = true)
    public Boolean getPreviousPageCheck(SearchType searchType, String keyword, Pageable pageable){
        if(keyword != null) {
            Page<PostListResponseDto> saved = search(searchType, keyword, pageable);
            Boolean check = saved.hasPrevious(); // 다음 페이지가 있는지 확인 있으면 True, 없으면 False
            return check;
        }
        Page<PostListResponseDto> saved = search(SearchType.ALL,null, pageable);
        Boolean check = saved.hasPrevious();
        return check;
    }
    //게시글 저장
    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postRepository.save(requestDto.toEntity()).getId(); //dto는 entity로 변환해서 저장할 것
    }

    //게시글 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id)
        );
        posts.update(requestDto.getTitle(), requestDto.getContents());
        return id;
    }

    //id에 따른 게시글 삭제
    @Transactional
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public Map<String, String> validateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error: errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }
}
