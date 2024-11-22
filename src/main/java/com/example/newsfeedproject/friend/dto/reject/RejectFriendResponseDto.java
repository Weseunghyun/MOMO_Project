package com.example.newsfeedproject.friend.dto.reject;

import lombok.Getter;

@Getter
public class RejectFriendResponseDto {

    private final boolean success;

    public RejectFriendResponseDto(boolean success) {
        this.success = success;
    }
}
