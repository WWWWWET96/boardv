package com.example.boardv.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsUpdateRequestDto {
    //author는 변경되면 안됨

    private String title;
    private String contents;


    @Builder
    public PostsUpdateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
