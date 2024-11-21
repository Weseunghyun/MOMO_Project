package com.example.newsfeedproject.friend.controller;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendResponseDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendRequestDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;
    @Autowired
    private FriendController(FriendService friendService) {
        this.friendService = friendService;
    }
    @ExceptionHandler(ResponseStatusException.class)
    // 에러 메시지 반환을 위한 메소드
    public ResponseEntity<String> handleException(ResponseStatusException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "유효성 검사 실패";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentTypeMismatchException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효성 검사 실패");
    }

    // 친구 요청
    @GetMapping("/request")
    public ResponseEntity<RequestFriendResponseDto> requestFriend(@Validated @RequestBody RequestFriendRequestDto requestFriendRequestDto,
                                                                  BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpSession session = request.getSession(false);
        RequestFriendServiceDto serviceDto = new RequestFriendServiceDto(requestFriendRequestDto.getReceiverId(),(Long)session.getAttribute("userId"));
        return new ResponseEntity<>(friendService.RequestFriend(serviceDto), HttpStatus.OK);
    }

    // 친구 요청 수락
    @PostMapping("/request/{friendId}")
    public ResponseEntity<AcceptFriendResponseDto> acceptFriend(@PathVariable("friendId") Long friendId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        AcceptFriendServiceDto serviceDto = new AcceptFriendServiceDto((Long)session.getAttribute("userId"), friendId);
        return new ResponseEntity<>(friendService.AcceptFriend(serviceDto), HttpStatus.OK);
    }

    // 친구 요청 거절
    @DeleteMapping("/request/{friendId}")
    public ResponseEntity<RejectFriendResponseDto> rejectFriend(@PathVariable("friendId") Long friendId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        RejectFriendServiceDto serviceDto = new RejectFriendServiceDto((Long)session.getAttribute("userId"), friendId);
        return new ResponseEntity<>(friendService.RejectFriend(serviceDto), HttpStatus.OK);
    }

}
