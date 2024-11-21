package com.example.newsfeedproject.friend.dto.findpost;

import com.example.newsfeedproject.post.entity.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class FindPostResponseDto {
    private final List<Post> posts;
    public FindPostResponseDto(List<Post> posts) {
        this.posts = posts;
    }
}
