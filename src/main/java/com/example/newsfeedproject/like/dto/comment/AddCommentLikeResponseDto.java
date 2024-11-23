package com.example.newsfeedproject.like.dto.comment;

import lombok.Getter;

@Getter
public class AddCommentLikeResponseDto {
    private final boolean success ;
    public AddCommentLikeResponseDto(boolean success) {
        this.success = success;
    }
}
