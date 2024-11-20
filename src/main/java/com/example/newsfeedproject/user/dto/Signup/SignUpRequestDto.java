package com.example.newsfeedproject.user.dto.Signup;

import lombok.Getter;

@Getter
public class SignUpRequestDto {

    private final String name;

    private final String email;

    private final String profileImageUrl;

    private final String password;

    public SignUpRequestDto(String name, String email, String profileImageUrl, String password) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.password = password;
    }
}
