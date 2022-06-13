package com.example.boardv.domain;


import com.example.boardv.config.auth.domain.BaseTimeEntity;
import com.example.boardv.config.auth.domain.user.General.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name ="posts")
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String author;

    @Column(columnDefinition = "TEXT")
    private String contents;


    @Column(length = 30, nullable = false)
    private String title;

@Builder
    public Posts(Long id, String author, String contents, String title) {
        this.id = id;
        this.author = author;
        this.contents = contents;
        this.title = title;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
