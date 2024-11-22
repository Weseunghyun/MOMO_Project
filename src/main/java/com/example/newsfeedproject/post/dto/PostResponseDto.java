package com.example.newsfeedproject.post.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    public PostResponseDto(Long id, Long userId, String title, String content,
        LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

}
