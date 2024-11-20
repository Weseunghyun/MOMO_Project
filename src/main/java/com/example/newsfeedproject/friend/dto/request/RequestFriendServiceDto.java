package com.example.newsfeedproject.friend.dto.request;

import lombok.Getter;

@Getter
public class RequestFriendServiceDto {
    private final Long receiverId;
    private final Long requesterId;
    public RequestFriendServiceDto(Long receiverId, Long requesterId) {
        this.receiverId = receiverId;
        this.requesterId = requesterId;
    }
}
