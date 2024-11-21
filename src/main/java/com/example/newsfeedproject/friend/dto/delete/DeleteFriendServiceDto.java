package com.example.newsfeedproject.friend.dto.delete;

import lombok.Getter;

@Getter
public class DeleteFriendServiceDto {
    private final Long userId;
    private final Long friendId;
    public DeleteFriendServiceDto(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
