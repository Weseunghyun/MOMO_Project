package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.Profile.ProfileResponseDto;
import com.example.newsfeedproject.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/api/users/profiles/{userId}")
    public ResponseEntity<ProfileResponseDto> findUserProfile(@PathVariable Long userId) {

        ProfileResponseDto responseDto = userProfileService.findUserProfile(userId);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
