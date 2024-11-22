package com.example.newsfeedproject.friend.dto.reject;

import lombok.Getter;

@Getter
public class RejectFriendServiceDto {

    private final Long friendId;
    private final Long userId;

    public RejectFriendServiceDto(Long friendId, Long userId) {
        this.friendId = friendId;
        this.userId = userId;
    }
}
