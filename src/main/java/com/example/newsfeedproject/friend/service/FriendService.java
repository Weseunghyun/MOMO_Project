package com.example.newsfeedproject.friend.service;

import com.example.newsfeedproject.friend.dto.request.RequestFriendRequestDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.repository.FriendRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class FriendService {
    private FriendRepository friendRepository;
    private final UserRepository userRepository;
    public FriendService(FriendRepository friendRepository , UserRepository userRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    public RequestFriendResponseDto RequestFriend(RequestFriendServiceDto dto) {
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
                );
        User requester = userRepository.findById(dto.getReceiverId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
                );
        Friend friend = new Friend(receiver, requester);
        friendRepository.save(friend);
        return new RequestFriendResponseDto(true);
    }
}
