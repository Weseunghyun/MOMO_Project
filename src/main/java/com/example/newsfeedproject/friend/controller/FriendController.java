package com.example.newsfeedproject.friend.controller;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendRequestDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendServiceDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostResponseDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostServiceDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendResponseDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendRequestDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 친구 요청
    @PostMapping("/request")
    public ResponseEntity<RequestFriendResponseDto> requestFriend(
        @Validated @RequestBody RequestFriendRequestDto requestFriendRequestDto,
        HttpServletRequest request
    ) {

        HttpSession session = request.getSession(false);

        RequestFriendServiceDto serviceDto = new RequestFriendServiceDto(
            requestFriendRequestDto.getReceiverId(),
            (Long) session.getAttribute("userId")
        );

        return new ResponseEntity<>(friendService.requestFriend(serviceDto), HttpStatus.OK);
    }

    // 친구 요청 수락
    @PostMapping("/request/{friendId}")
    public ResponseEntity<AcceptFriendResponseDto> acceptFriend(
        @PathVariable("friendId") Long friendId,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        AcceptFriendServiceDto serviceDto = new AcceptFriendServiceDto(
            (Long) session.getAttribute("userId"), friendId);

        friendService.acceptFriend(serviceDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 요청 거절
    @DeleteMapping("/{friendId}")
    public ResponseEntity<RejectFriendResponseDto> rejectFriend(
        @PathVariable("friendId") Long friendId,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        RejectFriendServiceDto serviceDto = new RejectFriendServiceDto(
            friendId,
            (Long) session.getAttribute("userId")
        );

        return new ResponseEntity<>(friendService.rejectFriend(serviceDto), HttpStatus.OK);
    }

    // 친구 게시글 조회
    @GetMapping("/newsfeed")
    public ResponseEntity<FindPostResponseDto> findPosts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        FindPostServiceDto serviceDto = new FindPostServiceDto(
            (Long) session.getAttribute("userId"),
            page,
            size
        );

        FindPostResponseDto responseDto = friendService.findPost(serviceDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 친구 삭제
    @DeleteMapping("/remove")
    public ResponseEntity<DeleteFriendRequestDto> deleteFriend(
        @Validated @RequestBody DeleteFriendRequestDto dto,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        DeleteFriendServiceDto serviceDto = new DeleteFriendServiceDto(
            (Long) session.getAttribute("userId"),
            dto.getFriendId()
        );

        friendService.deleteFriend(serviceDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
