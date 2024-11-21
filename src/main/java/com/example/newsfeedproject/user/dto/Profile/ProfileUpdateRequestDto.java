package com.example.newsfeedproject.user.dto.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequestDto{
    private final String userName;
    private final String profileImageUrl;
    private final String password;
    private final String newPassword;


    public ProfileUpdateRequestDto(String userName, String profileImageUrl, String password, String newPassword) {
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.password = password;
        this.newPassword = newPassword;
    }
}
