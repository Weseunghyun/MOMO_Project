package com.example.newsfeedproject.like.dto.comment;

import lombok.Getter;

@Getter
public class DeleteCommentLikeResponseDto {
    private final boolean success;
    public DeleteCommentLikeResponseDto(boolean success) {
        this.success = success;
    }
}
