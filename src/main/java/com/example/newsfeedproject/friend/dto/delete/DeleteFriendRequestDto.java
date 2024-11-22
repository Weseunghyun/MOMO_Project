package com.example.newsfeedproject.friend.dto.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DeleteFriendRequestDto {

    private final Long friendId;

    public DeleteFriendRequestDto(@JsonProperty("friendId") Long friendId) {
        this.friendId = friendId;
    }
}
