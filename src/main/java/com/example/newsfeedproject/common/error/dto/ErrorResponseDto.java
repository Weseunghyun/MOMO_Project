package com.example.newsfeedproject.common.error.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

//사용자에게 일관된 형식으로 에러를 반환하는데 사용
@Getter
@Builder
public class ErrorResponseDto {

    private int errorCode;            // HTTP 상태 코드
    private String message;        // 에러 메시지
    private String path;           // 요청 URL
    private LocalDateTime errorTime; // 에러 발생 시간
}
