package com.example.newsfeedproject.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private final String content; // 내용

    public CommentRequestDto(@JsonProperty("content") String content) {
        this.content = content;
    }
}
