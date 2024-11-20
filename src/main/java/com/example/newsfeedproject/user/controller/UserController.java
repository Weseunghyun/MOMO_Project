package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.Signup.SignUpRequestDto;
import com.example.newsfeedproject.user.dto.Signup.SignUpResponseDto;
import com.example.newsfeedproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto signUpResponseDto =
                userService.signUp(
                        requestDto.getName(),
                        requestDto.getEmail(),
                        requestDto.getProfileImageUrl(),
                        requestDto.getPassword()
                        );
        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }
}
