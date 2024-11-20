package com.example.newsfeedproject.post.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PostPageResponseDto {

    private final List<PostWithNameResponseDto> posts;
    private final PageInfo pageInfo;

    public PostPageResponseDto(List<PostWithNameResponseDto> content, PageInfo pageInfo) {
        this.posts = content;
        this.pageInfo = pageInfo;
    }

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private int page;
        private int size;
        private int totalElements;
        private int totalPages;
    }
}
