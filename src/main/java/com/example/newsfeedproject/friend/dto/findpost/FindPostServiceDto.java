package com.example.newsfeedproject.friend.dto.findpost;

import lombok.Getter;

@Getter
public class FindPostServiceDto {
    private final Long userId;
    public FindPostServiceDto(Long userId) {
        this.userId = userId;
    }
}
