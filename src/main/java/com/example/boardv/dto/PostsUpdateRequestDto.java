package com.example.boardv.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class PostsUpdateRequestDto {
    //author는 변경되면 안됨

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 30, message = "제목은 30자 이하로 입력해주세요")
    private String title;


    private String contents;


    @Builder
    public PostsUpdateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
