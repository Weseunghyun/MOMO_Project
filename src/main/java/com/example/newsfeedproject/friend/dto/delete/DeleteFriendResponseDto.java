package com.example.newsfeedproject.friend.dto.delete;

import lombok.Getter;

@Getter
public class DeleteFriendResponseDto {

    private final boolean success;

    public DeleteFriendResponseDto(boolean success) {
        this.success = success;
    }
}
