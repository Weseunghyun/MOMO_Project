package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.Login.LoginRequestDto;
import com.example.newsfeedproject.user.dto.Signup.SignUpRequestDto;
import com.example.newsfeedproject.user.dto.Signup.SignUpResponseDto;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        @RequestBody String password,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인해주세요.");
        }

        Long userId = (Long) session.getAttribute("userId");

        if (!userId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("계정 삭제를 할 수 없습니다.");

        }

        userService.deleteUser(id, password, request);
        session.invalidate();
        return ResponseEntity.ok().body("정상적으로 삭제되었습니다.");
    }

}


