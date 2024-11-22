package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.profile.ProfileResponseDto;
import com.example.newsfeedproject.user.dto.profile.ProfileUpdateRequestDto;
import com.example.newsfeedproject.user.dto.profile.ProfileUpdateResponseDto;
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

    //사용자 id로 프로필 정보 조회
    @GetMapping("{userId}")
    public ResponseEntity<ProfileResponseDto> findUserProfile(@PathVariable Long userId) {

        ProfileResponseDto responseDto = userProfileService.findUserProfile(userId);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    //사용자 프로필 정보, 사용자 비밀번호 수정
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
