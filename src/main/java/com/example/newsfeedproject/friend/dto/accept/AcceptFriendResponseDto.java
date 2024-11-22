package com.example.newsfeedproject.friend.dto.accept;

import lombok.Getter;

@Getter
public class AcceptFriendResponseDto {

    private final boolean success;

    public AcceptFriendResponseDto(boolean success) {
        this.success = success;
    }
}
