package com.example.newsfeedproject.like.dto.comment;

import lombok.Getter;

@Getter
public class AddCommentLikeServiceDto {
    private final Long commentId;
    private final Long UserId;
    public AddCommentLikeServiceDto(Long postId, Long userId) {
        commentId = postId;
        UserId = userId;
    }
}
