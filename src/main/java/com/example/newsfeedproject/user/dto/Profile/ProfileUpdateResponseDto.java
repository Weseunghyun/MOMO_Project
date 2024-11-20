package com.example.newsfeedproject.user.dto.Profile;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileUpdateResponseDto{
    private final Long userId;
    private final String userName;
    private final String profileImageUrl;
    private final LocalDateTime modifiedAt;

    public ProfileUpdateResponseDto(Long userId, String userName, String profileImageUrl, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.modifiedAt = modifiedAt;
    }
}
