package com.example.newsfeedproject.like.dto.post;

import lombok.Getter;

@Getter
public class DeletePostLikeResponseDto {
    private final boolean success;
    public DeletePostLikeResponseDto(boolean success) {
        this.success = success;
    }
}
