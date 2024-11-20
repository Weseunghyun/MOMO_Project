package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.user.dto.ProfileResponseDto;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class UserProfileService {

        private final UserRepository userRepository;

        public ProfileResponseDto findUserProfile(Long userId) {
            User findUser = userRepository.findById(userId).orElseThrow(null);

            return new ProfileResponseDto(
                    findUser.getId(),
                    findUser.getName(),
                    findUser.getEmail(),
                    findUser.getProfileImageUrl()
            );
        }
}
