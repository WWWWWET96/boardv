package com.example.boardv.dto;

import com.example.boardv.config.auth.domain.user.General.User;
import com.example.boardv.domain.Posts;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsSaveRequestDto {

    private String title;
    private String contents;

    private String author;
    private User user;
    @Builder
    public PostsSaveRequestDto(String title, String contents, String author) {
        this.title = title;
        this.contents = contents;
        this.author = author;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .contents(contents)
                .author(author)
                .build();
    }

}
