package com.example.newsfeedproject.like.dto.post;

import lombok.Getter;

@Getter
public class DeletePostLikeServiceDto {
    private final Long PostId;
    private final Long UserId;
    public DeletePostLikeServiceDto(Long postId, Long userId) {
        PostId = postId;
        UserId = userId;
    }
}
