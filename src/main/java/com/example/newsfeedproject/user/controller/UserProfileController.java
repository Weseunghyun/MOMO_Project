package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.Profile.ProfileResponseDto;
import com.example.newsfeedproject.user.dto.Profile.ProfileUpdateRequestDto;
import com.example.newsfeedproject.user.dto.Profile.ProfileUpdateResponseDto;
import com.example.newsfeedproject.user.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * 사용자 ID로 프로필 조회
     */
    @GetMapping("{userId}")
    public ResponseEntity<ProfileResponseDto> findUserProfile(@PathVariable Long userId) {

        ProfileResponseDto responseDto = userProfileService.findUserProfile(userId);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 사용자 프로필 수정
     */
    @PutMapping()
    public ResponseEntity<ProfileUpdateResponseDto> updateUserProfile(
        HttpServletRequest request,
        @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        ProfileUpdateResponseDto responseDto = userProfileService.updateUserProfile(
            request,
            requestDto.getUserName(),
            requestDto.getProfileImageUrl(),
            requestDto.getPassword(),
            requestDto.getNewPassword()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
