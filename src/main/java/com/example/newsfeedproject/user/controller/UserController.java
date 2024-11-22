package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.delete.UserDeleteRequestDto;
import com.example.newsfeedproject.user.dto.login.LoginRequestDto;
import com.example.newsfeedproject.user.dto.signup.SignUpRequestDto;
import com.example.newsfeedproject.user.dto.signup.SignUpResponseDto;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
        @RequestBody LoginRequestDto loginRequestDto,
        HttpServletRequest request
    ) {

        userService.loginUser(loginRequestDto, request);

        return ResponseEntity.ok().body("정상적으로 로그인되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("로그아웃 성공");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
        @PathVariable Long id,
        @RequestBody UserDeleteRequestDto requestDto,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        userService.deleteUser(id, requestDto.getPassword(), request);

        session.invalidate();
        return ResponseEntity.ok().body("정상적으로 삭제되었습니다.");
    }

}


