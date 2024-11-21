package com.example.newsfeedproject.friend.dto.accept;

import lombok.Getter;

@Getter
public class AcceptFriendServiceDto {
    private final Long userId;
    private final Long friendId;
    public AcceptFriendServiceDto(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
