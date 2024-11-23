package com.example.newsfeedproject.comment.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateCommentResponseDto {

    private final Long id;
    private final String userName;
    private final String content;
    private final LocalDateTime updatedAt;

    public UpdateCommentResponseDto(Long id, String userName, String content,
        LocalDateTime updatedAt) {
        this.id = id;
        this.userName = userName;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
