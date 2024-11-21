package com.example.newsfeedproject.post.dto;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    private final String title;
    private final String content;
    private final String password;

    public PostUpdateRequestDto(String title, String content, String password) {
        this.title = title;
        this.content = content;
        this.password = password;
    }
}
