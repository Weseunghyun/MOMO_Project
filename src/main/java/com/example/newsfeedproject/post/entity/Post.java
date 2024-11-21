package com.example.newsfeedproject.post.entity;

import com.example.newsfeedproject.common.entity.TimeBaseEntity;
import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "post") // 테이블 이름 매핑
public class Post extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유 식별자

    @Setter
    @Column(nullable = false)
    private String title; // 게시글 제목

    @Setter
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 (ManyToOne 관계)

    public Post() {

    }

    public Post(String title, String content, User postUser) {
        this.title = title;
        this.content = content;
        this.user = postUser;
    }
}
