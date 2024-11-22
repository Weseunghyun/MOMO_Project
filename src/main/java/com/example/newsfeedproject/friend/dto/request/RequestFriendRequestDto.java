package com.example.newsfeedproject.friend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RequestFriendRequestDto {

    private final Long receiverId;

    public RequestFriendRequestDto(@JsonProperty("receiverId") Long receiverId) {
        this.receiverId = receiverId;
    }
}
