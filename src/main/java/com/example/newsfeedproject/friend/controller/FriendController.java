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

    // 친구 요청을 하는 엔드포인트
    @PostMapping("/request")
    public ResponseEntity<RequestFriendResponseDto> requestFriend(
        @Validated @RequestBody RequestFriendRequestDto requestFriendRequestDto,
        HttpServletRequest request
    ) {
        //세션을 통해 현재 유저 아이디를 가져옴
        HttpSession session = request.getSession(false);
        // 서비스에서 필요한 정보는 요청을 받을 대상의 userId와 자기 자신의 userId
        RequestFriendServiceDto serviceDto = new RequestFriendServiceDto(
            requestFriendRequestDto.getReceiverId(),
            (Long) session.getAttribute("userId")
        );

        return new ResponseEntity<>(friendService.requestFriend(serviceDto), HttpStatus.OK);
    }

    // 친구 요청 수락하는 엔드포인트
    @PostMapping("/request/{friendId}")
    public ResponseEntity<AcceptFriendResponseDto> acceptFriend(
        @PathVariable("friendId") Long friendId,
        HttpServletRequest request
    ) {
        //세션을 통해 현재 유저 아이디를 가져옴
        HttpSession session = request.getSession(false);
        // 서비스에서 필요한 정보는 자신의 userId와 friendId
        AcceptFriendServiceDto serviceDto = new AcceptFriendServiceDto(
            (Long) session.getAttribute("userId"), friendId);

        return new ResponseEntity<>(friendService.acceptFriend(serviceDto),HttpStatus.OK);
    }

    // 친구 요청 거절하는 엔드포인트
    @DeleteMapping("/{friendId}")
    public ResponseEntity<RejectFriendResponseDto> rejectFriend(
        @PathVariable("friendId") Long friendId,
        HttpServletRequest request
    ) {
        // 세션을 통해 현재 유저의 아이디를 가져옴
        HttpSession session = request.getSession(false);
        // 서비스에서 필요한 정보는 자신의 userId와 friendId
        RejectFriendServiceDto serviceDto = new RejectFriendServiceDto(
            friendId,
            (Long) session.getAttribute("userId")
        );

        return new ResponseEntity<>(friendService.rejectFriend(serviceDto), HttpStatus.OK);
    }

    // 페이징 처리된 친구 게시글 목록을 반환하는 엔드포인트. 디폴트 값 page=1, size=10
    @GetMapping("/newsfeed")
    public ResponseEntity<FindPostResponseDto> findPosts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        HttpServletRequest request
    ) {
        // 세션을 통해 현재 유저의 아이디를 가져옴
        HttpSession session = request.getSession(false);
        // 서비스에서 필요한 정보는 자신의 userId와 page와 size
        FindPostServiceDto serviceDto = new FindPostServiceDto(
            (Long) session.getAttribute("userId"),
            page -1,
            size
        );

        FindPostResponseDto responseDto = friendService.findPost(serviceDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 친구 삭제하는 엔드포인트
    @DeleteMapping("/remove")
    public ResponseEntity<DeleteFriendRequestDto> deleteFriend(
        @Validated @RequestBody DeleteFriendRequestDto dto,
        HttpServletRequest request
    ) {
        // 세션을 통해 현재 유저의 아이디를 가져옴
        HttpSession session = request.getSession(false);
        // 서비스에서 필요한 정보는 자신의 userId와 friendId
        DeleteFriendServiceDto serviceDto = new DeleteFriendServiceDto(
            (Long) session.getAttribute("userId"),
            dto.getFriendId()
        );

        friendService.deleteFriend(serviceDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
