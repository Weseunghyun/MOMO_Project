package com.example.newsfeedproject.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostDeleteRequestDto {

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private final String password;

    public PostDeleteRequestDto(@JsonProperty("password") String password) {
        this.password = password;
    }
}
