package com.example.boardv.service;


import com.example.boardv.domain.Posts;
import com.example.boardv.dto.PostListResponseDto;
import com.example.boardv.dto.PostsResponseDto;
import com.example.boardv.dto.PostsSaveRequestDto;
import com.example.boardv.domain.PostsRepository;
import com.example.boardv.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postRepository;

    public List<PostListResponseDto> findAllDesc() {
        return
        postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postRepository.save(requestDto.toEntity()).getId(); //dto는 entity로 변환해서 저장할 것
    }
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id)
        );

        posts.update(requestDto.getTitle(), requestDto.getContents());
        return id;

    }
    public PostsResponseDto findById(Long id) {
        Posts entity = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id=" +id));

        return new PostsResponseDto(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }


    @Transactional
    public void deleteAll() {
    postRepository.deleteAll();
    }
}
