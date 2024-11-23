package com.example.newsfeedproject.comment.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String username;
    private final String content;
    private final LocalDateTime createdAt;
    private final Long likescount;
    public CommentResponseDto(Long id, String username, String content, LocalDateTime createdAt, Long likescount) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.likescount = likescount;
    }
}
