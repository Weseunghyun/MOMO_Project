package com.example.newsfeedproject.like.dto.post;

import lombok.Getter;

@Getter
public class AddPostLikeResponseDto {
    private final boolean success ;
    public AddPostLikeResponseDto(boolean success) {
        this.success = success;
    }
}
