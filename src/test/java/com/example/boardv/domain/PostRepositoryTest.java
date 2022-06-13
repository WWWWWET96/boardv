package com.example.boardv.domain;

import com.example.boardv.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;


import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostsRepository postRepository;



    @AfterAll
    public void cleanup(){
        postRepository.deleteAll();
    }


    @Test
    public void 게시글저장_불러오기(){
        //given
        String title = "title";
        String author = "author";

        postRepository.save(Posts.builder()
                .title(title)
                .author(author)
                .build());

        //when
        List<Posts> postsList = postRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getAuthor()).isEqualTo(author);

        }


    @Test
    public void BaseTimeEntity_등록(){
        //given
        LocalDateTime now = LocalDateTime.of(2022,6,9,0,0,0);
        postRepository.save(Posts.builder()
                .title("title")
                .contents("content")
                .author("author")
                .build());

        //when
        List<Posts> postsList = postRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>>>createDate="+posts.getCreateTime()+"modifiedDate="+posts.getModifiedDate());

        assertThat(posts.getCreateTime()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}
