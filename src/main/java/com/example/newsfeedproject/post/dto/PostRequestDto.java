package com.example.newsfeedproject.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @NotBlank(message = "게시글 제목은 필수값 입니다.")
    @Size(min = 2, max = 15, message = "제목은 2자 이상 15자 이하여야 합니다.")
    private final String title;

    @NotBlank(message = "게시글 본문 내용은 필수값 입니다.")
    private final String content;

    public PostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
