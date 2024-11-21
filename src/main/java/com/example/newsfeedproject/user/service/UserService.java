package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.config.PasswordEncoder;
import com.example.newsfeedproject.common.util.UtilValidation;
import com.example.newsfeedproject.user.dto.Signup.SignUpResponseDto;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public SignUpResponseDto signUp(String name, String email, String profileImageUrl, String password) {

//        // 비밀번호 형식
//        if (!UtilValidation.isValidPasswordFormat(password)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호는 최소 8자, 대소문자 포함한 영문, 숫자, 특수문자를 포함해야합니다.");
//        }

        User user = new User(name, email, profileImageUrl, passwordEncoder.encode(password)); //password에 대한 유효성 검사 진행 후 문제 없으면 저장

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getProfileImageUrl(), savedUser.getCreatedAt(), savedUser.getModifiedAt());
    }

}
