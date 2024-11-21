package com.example.newsfeedproject.friend.dto.request;

import lombok.Getter;

@Getter
public class RequestFriendResponseDto {
    private boolean success;
    public RequestFriendResponseDto(boolean success) {
       this.success = success;
    }
}
