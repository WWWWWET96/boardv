package com.example.boardv.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

/*
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC ")
    List<Posts> findAllDesc( );
*/


}


