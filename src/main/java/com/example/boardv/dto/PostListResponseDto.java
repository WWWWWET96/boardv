package com.example.boardv.dto;

import com.example.boardv.domain.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long id;
    private String title;

    private String author;
    private LocalDateTime modifiedDate;

    @Builder
    public PostListResponseDto(Posts entity){

        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
