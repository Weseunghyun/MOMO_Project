package com.example.newsfeedproject.friend.dto.findpost;

import com.example.newsfeedproject.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FindPostResponseDto {
    private final Long postId;
    private final String author;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    public FindPostResponseDto(Long postId, String author, String title, String content, LocalDateTime createdAt) {
        this.postId = postId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
