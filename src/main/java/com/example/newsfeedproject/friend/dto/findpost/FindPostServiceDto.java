package com.example.newsfeedproject.friend.dto.findpost;

import lombok.Getter;

@Getter
public class FindPostServiceDto {
    private final Long userId;
    private final int page;
    private final int size;
    public FindPostServiceDto(Long userId, int page, int size) {
        this.userId = userId;
        this.page = page;
        this.size = size;
    }
}
