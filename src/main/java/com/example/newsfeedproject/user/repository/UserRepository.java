package com.example.newsfeedproject.user.repository;

import com.example.newsfeedproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface UserRepository extends JpaRepository<User, Long> {

    // ID로 Post 조회 시 존재하지 않으면 NOT_FOUND 예외 발생
    default User findByIdOrElseThrow(long id) {
        return findById(id).
            orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found")
            );
    }
}
