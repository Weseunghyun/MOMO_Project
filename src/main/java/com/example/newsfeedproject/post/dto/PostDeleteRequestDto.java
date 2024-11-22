package com.example.newsfeedproject.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PostDeleteRequestDto {

    private final String password;

    public PostDeleteRequestDto(@JsonProperty("password") String password) {
        this.password = password;
    }
}
