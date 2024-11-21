package com.example.newsfeedproject.friend.service;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostResponseDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostServiceDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendResponseDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendRequestDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.repository.FriendRepository;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;


    public FriendService(FriendRepository friendRepository , UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;

    }

    public RequestFriendResponseDto requestFriend(RequestFriendServiceDto dto) {
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "받는 사람의 유저를 찾을 수 없습니다.")
                );
        User requester = userRepository.findById(dto.getRequesterId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "보내는 사람의 유저를 찾을 수 없습니다.")
                );
        Friend friend = new Friend(receiver, requester);
        friendRepository.save(friend);
        return new RequestFriendResponseDto(true);
    }

    @Transactional
    public AcceptFriendResponseDto acceptFriend(AcceptFriendServiceDto dto) {
        User accepter = userRepository.findById(dto.getUserId())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "수락하는 유저를 찾을 수 없습니다.")
                );
        // receiver의 유저 id로부터 friend를 찾고 status 변경
        Friend acceptableFriend = accepter.getReceivedFriendRequests().stream().filter(friend -> friend.getId().equals(dto.getFriendId())).findFirst()
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "수락할 친구를 찾을 수 없습니다.")
                );
        acceptableFriend.accept();
        friendRepository.save(acceptableFriend);
        return new AcceptFriendResponseDto(true);
    }

    public RejectFriendResponseDto rejectFriend(RejectFriendServiceDto dto) {

        List<Friend> requestFriends = friendRepository.findAllFriendRequests(dto.getUserId());
        Friend requestFriend = requestFriends.stream().filter(friend -> friend.getId().equals(dto.getFriendId())).findFirst()
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "거절할 요청을 찾을 수 없습니다.")
                );
        friendRepository.delete(requestFriend);

        System.out.println(friendRepository.count());
        return new RejectFriendResponseDto(true);
    }

    @Transactional
    public FindPostResponseDto findPost(FindPostServiceDto dto) {

        List<Friend> friends = friendRepository.findAllFriendRequests(dto.getUserId())
                .stream().filter(friend -> friend.getStatus().equals(Friend.FriendStatus.ACCEPTED)).collect(Collectors.toList());
        List<Post> posts = new ArrayList<>();
        for(Friend friend : friends) {
            // 친구 관계인 상대를 찾기 위해 자기의 id와 receiverId를 비교하여 같은경우 requesterId가 상대임
            Long targetId = Objects.equals(friend.getReceiver().getId(), dto.getUserId()) ?  friend.getRequester().getId() : friend.getReceiver().getId();
            User targetUser = userRepository.findByIdOrElseThrow(targetId);
            // 친구의 포스트들을 모두 posts에 넣음
            posts.addAll(targetUser.getPosts());
        }
        return new FindPostResponseDto(posts);
    }
}
