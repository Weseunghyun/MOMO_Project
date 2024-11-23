package com.example.newsfeedproject.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String username;
    private final String comment;
    private final LocalDateTime createdAt;

    public CommentResponseDto(Long id, String username, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
