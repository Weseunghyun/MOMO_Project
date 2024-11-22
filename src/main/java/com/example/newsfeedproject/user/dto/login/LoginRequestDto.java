package com.example.newsfeedproject.user.dto.login;

import lombok.Getter;

@Getter
public class LoginRequestDto {

    private final String email;

    private final String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
