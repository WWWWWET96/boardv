package com.example.boardv.domain;

import com.example.boardv.dto.PostListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findByTitleContaining (String keyword, Pageable pageable);
    //Containing을 붙여주면 Like검색이 가능해짐

    Page<Posts> findByAuthorContaining(String keyword, Pageable pageable);

}


