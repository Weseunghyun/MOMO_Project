package com.example.newsfeedproject.user.dto.Signup;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpResponseDto {

    private final Long id;

    private final String name;

    private final String email;

    private final String profileImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public SignUpResponseDto(Long id, String name, String email, String profileImageUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

