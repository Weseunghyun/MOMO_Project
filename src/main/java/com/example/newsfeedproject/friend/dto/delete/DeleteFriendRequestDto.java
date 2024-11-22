package com.example.newsfeedproject.friend.dto.delete;

import lombok.Getter;

@Getter
public class DeleteFriendRequestDto {

    private final Long friendId;

    public DeleteFriendRequestDto(Long friendId) {
        this.friendId = friendId;
    }
}
