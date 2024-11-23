package com.example.newsfeedproject.like.dto.post;

import lombok.Getter;

@Getter
public class AddPostLikeServiceDto {
    private final Long PostId;
    private final Long UserId;
    public AddPostLikeServiceDto(Long postId, Long userId) {
        PostId = postId;
        UserId = userId;
    }
}
