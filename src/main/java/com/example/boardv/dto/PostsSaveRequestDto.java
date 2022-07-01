package com.example.boardv.dto;

import com.example.boardv.config.auth.domain.user.General.User;
import com.example.boardv.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostsSaveRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 30, message = "제목은 30자 이하로 입력해주세요")
    private String title;

    private String contents;

    private String author;

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
