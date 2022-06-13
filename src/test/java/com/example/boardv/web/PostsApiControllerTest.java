package com.example.boardv.web;


import com.example.boardv.domain.Posts;
import com.example.boardv.domain.PostsRepository;
import com.example.boardv.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class PostsApiControllerTest {

/*    @LocalServerPort
    private int port;*/

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;

    PostsApiControllerTest(TestRestTemplate testRestTemplate, PostsRepository postsRepository) {
        this.testRestTemplate = testRestTemplate;
        this.postsRepository = postsRepository;
    }

    /*   @AfterAll
       public void tearDown() throws Exception{
           postsRepository.deleteAll();
       }

       @Test
       public void Posts_등록된다() throws Exception{
           //given
           String title = "title";
           String author = "author";
           PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                   .title(title)
                   .author(author)
                   .build();

           String url = "http://localhost:" + port + "/api/v1/posts";

           //when
           ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, requestDto, Long.class);
           //then
           assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
           assertThat(responseEntity.getBody()).isGreaterThan(0L);

           List<Posts> all = postsRepository.findAll();
           assertThat(all.get(0).getTitle()).isEqualTo(title);
           assertThat(all.get(0).getAuthor()).isEqualTo(author);

       }*/

    @ParameterizedTest
    public void Posts_수정된다() throws Exception{
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title1")
                .contents("contents1")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .contents(expectedContent)
                .build();

        String url = "http://localhost:8080/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestDtoHttpEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, requestDtoHttpEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContents()).isEqualTo(expectedContent);
    }
}