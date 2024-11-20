package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.config.PasswordEncoder;
import com.example.newsfeedproject.user.dto.Profile.ProfileResponseDto;
import com.example.newsfeedproject.user.dto.Profile.ProfileUpdateResponseDto;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    /**
     * 사용자 프로필 조회
     */

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponseDto findUserProfile(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(null);

        return new ProfileResponseDto(
                findUser.getId(),
                findUser.getName(),
                findUser.getEmail(),
                findUser.getProfileImageUrl()
        );
    }

    /**
     * 사용자 프로필 수정
     */
    
    public ProfileUpdateResponseDto updateUserProfile(HttpServletRequest request, String userName, String profileImageUrl, String rawPassword) {
        HttpSession session = request.getSession(false); // 세션 o -> 현재 로그인된 사용자의 httpServletRequest를 가져옴
        if (session != null) {}

        // 현재 로그인하고 있는 사용자의 정보를 가져옴
        Long currentUserId = (Long) session.getAttribute("userId");

        User updateUser = userRepository.findById(currentUserId).orElseThrow(null); // user 찾아옴

        // 인코딩된 비밀번호와 일치하는지 검증
        if (!passwordEncoder.matches(rawPassword, updateUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        updateUser.setName(userName);
        updateUser.setProfileImageUrl(profileImageUrl);
        updateUser.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(updateUser);

        return new ProfileUpdateResponseDto(
                updateUser.getId(),
                updateUser.getName(),
                updateUser.getProfileImageUrl(),
                updateUser.getModifiedAt()
        );
    }
}
