package com.example.newsfeedproject.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {

    private final String comment; // 내용

    public CommentRequestDto(String comment) {
        this.comment = comment;
    }
}
