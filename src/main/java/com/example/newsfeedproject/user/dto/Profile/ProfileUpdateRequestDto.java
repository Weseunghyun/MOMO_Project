package com.example.newsfeedproject.user.dto.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequestDto{
    private final String userName;
    private final String profileImageUrl;
    private final String password;


    public ProfileUpdateRequestDto(String userName, String profileImageUrl, String password) {
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.password = password;
    }
}
