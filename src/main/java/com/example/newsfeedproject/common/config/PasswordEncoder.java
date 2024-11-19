package com.example.newsfeedproject.common.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    //원본 비밀번호를 입력받고 BCrypt 라이브러리를 이용해 해싱하는 메서드
    public String encode(String rawPassword) {
        //MIN_COST 를 사용하여 생성 비용을 최소화
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    //원본 비밀번호와 인코딩 비밀번호를 비교하여 검증하는 메서드
    public boolean matches(String rawPassword, String encodedPassword) {
        //verifyer를 사용, 입력된 비밀번호를 해싱 후에 인코딩 비밀번호와 일치하는지 확인
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}