package com.example.boardv.dto;

import com.example.boardv.domain.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String contents;

    private String author;

    public PostsResponseDto(Posts entity){ //생성자로 Entity를 받아 필드에 값을 넣음
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
        this.author=entity.getAuthor();
    }

}
