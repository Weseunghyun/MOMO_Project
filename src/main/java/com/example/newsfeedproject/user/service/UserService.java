package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.config.PasswordEncoder;
import com.example.newsfeedproject.common.util.UtilValidation;
import com.example.newsfeedproject.user.dto.Login.LoginRequestDto;
import com.example.newsfeedproject.user.dto.Signup.SignUpResponseDto;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public SignUpResponseDto signUp(String name, String email, String profileImageUrl, String password) {
        // 비밀번호 형식
        if (!UtilValidation.isValidPasswordFormat(password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호는 최소 8자, 대소문자 포함한 영문, 숫자, 특수문자를 포함해야합니다.");
        }
        User user = new User(name, email, profileImageUrl, passwordEncoder.encode(password));

//        /**
//         * TODO email 중복 검사 필요
//         *  -> 탈퇴한 회원도 포함해서 검사 해야함
//         */
        // SignUpResponseDto.getEmail() -> String
        // userRepository.findByEmail(email) -> User
        // email 중복 검사 -> email -> String == String

        User userByEmail = userRepository.findByEmail(email);

        if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "중복된 이메일입니다.");
        }

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getProfileImageUrl(), savedUser.getCreatedAt(), savedUser.getModifiedAt());
    }

//    /**
//     * 로그인을 처리하는 로직
//     * @param loginRequestDto
//     * @param request
//     */
    public void loginUser(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        // 로그인 시 탈퇴한 회원정보는 가져오지 않는다.
        User user = userRepository.findByEmailAndAndIsDeleted(loginRequestDto.getEmail(), false);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자");
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 비밀번호");

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
    }

    @Transactional
    public void deleteUser(Long id, String password, HttpServletRequest request) {
        User deleteUser = userRepository.findByIdOrElseThrow(id);

        HttpSession session = request.getSession();
        session.getAttribute("userId");


        if (passwordEncoder.matches(password, deleteUser.getPassword())) {
            deleteUser.setIsDeleted(true);
            userRepository.save(deleteUser);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

//        if (!deleteUser.getIsDeleted()) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원 탈퇴 실패");
//        }
    }
}






