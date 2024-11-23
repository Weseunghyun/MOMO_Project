package com.example.newsfeedproject.like.dto.comment;

import lombok.Getter;

@Getter
public class DeleteCommentLikeServiceDto {
    private final Long commentId;
    private final Long UserId;
    public DeleteCommentLikeServiceDto(Long postId, Long userId) {
        commentId = postId;
        UserId = userId;
    }
}
