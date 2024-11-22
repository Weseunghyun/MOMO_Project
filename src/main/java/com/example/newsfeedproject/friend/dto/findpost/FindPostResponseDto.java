package com.example.newsfeedproject.friend.dto.findpost;

import com.example.newsfeedproject.post.dto.PostPageResponseDto;
import com.example.newsfeedproject.post.dto.PostWithNameResponseDto;
import com.example.newsfeedproject.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FindPostResponseDto {

    private final List<PostWithNameResponseDto> posts;
    private final  PageInfo pageInfo;

    public FindPostResponseDto(List<PostWithNameResponseDto> content, PageInfo pageInfo) {
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