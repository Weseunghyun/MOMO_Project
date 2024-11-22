package com.example.newsfeedproject.friend.dto.request;

import lombok.Getter;

@Getter
public class RequestFriendRequestDto {

    private final Long receiverId;

    public RequestFriendRequestDto(Long receiverId) {
        this.receiverId = receiverId;
    }
}
