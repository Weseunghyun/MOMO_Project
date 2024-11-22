package com.example.newsfeedproject.friend.service;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendResponseDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendServiceDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostResponseDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostServiceDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendResponseDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.repository.FriendRepository;
import com.example.newsfeedproject.post.dto.PostWithNameResponseDto;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public RequestFriendResponseDto requestFriend(RequestFriendServiceDto dto) {
        User receiver = userRepository.findById(dto.getReceiverId())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "받는 사람의 유저를 찾을 수 없습니다.")
            );

        User requester = userRepository.findById(dto.getRequesterId())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "보내는 사람의 유저를 찾을 수 없습니다.")
            );

        if (receiver.equals(requester)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신에게 친구 요청을 할 수 없습니다");
        }

        List<Friend> friends = friendRepository.findAllFriendSent(dto.getRequesterId());
        friends.addAll(friendRepository.findAllFriendReceived(dto.getRequesterId()));

        // 이미 친구 요청을 하였거나 이미 친구상태인 경우 예외 발생
        for (Friend friend : friends) {
            if (friend.getRequester().equals(receiver) || friend.getReceiver().equals(receiver)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 대상에게 친구 요청을 하였습니다.");
            }
        }

        Friend friend = new Friend(requester, receiver);
        friendRepository.save(friend);

        return new RequestFriendResponseDto(true);
    }

    public AcceptFriendResponseDto acceptFriend(AcceptFriendServiceDto dto) {
        User accepter = userRepository.findById(dto.getUserId())
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "수락하는 유저를 찾을 수 없습니다.")
            );

        // receiver의 유저 id로부터 friend를 찾고 status 변경
        // 자신이 보낸 요청은 안보임
        Friend acceptableFriend = accepter.getReceivedFriendRequests().stream()
            .filter(friend -> friend.getId().equals(dto.getFriendId()))
            .filter(friend -> friend.getStatus().equals(Friend.FriendStatus.WAITING)).findFirst()
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "수락할 친구를 찾을 수 없습니다.")
            );

        acceptableFriend.accept();
        friendRepository.save(acceptableFriend);
        return new AcceptFriendResponseDto(true);
    }

    public RejectFriendResponseDto rejectFriend(RejectFriendServiceDto dto) {
        // 자신에게 요청이 온 waitting 상태의 friend를 가져옴
        List<Friend> requestFriends = friendRepository.findAllFriendReceivedWAITING(dto.getUserId());

        Friend requestFriend = requestFriends.stream()
            .filter(friend -> friend.getId().equals(dto.getFriendId()))
            .filter(friend -> friend.getStatus().equals(Friend.FriendStatus.WAITING)).findFirst()
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "거절할 요청을 찾을 수 없습니다.")
            );

        // waitting 상태의 friend 삭제
        friendRepository.delete(requestFriend);

        return new RejectFriendResponseDto(true);
    }


    public FindPostResponseDto findPost(FindPostServiceDto dto) {
        // 자신이 요청을 받거나 준 friend중 accepted 상태만 가져옴
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by("createdAt").descending());

        Page<Post> postPageFromReceiver = friendRepository.findFriendPostsFromReceiver(dto.getUserId(), pageable);
        Page<Post> postPageFromRequester = friendRepository.findFriendPostsFromRequester(dto.getUserId(), pageable);

        Page<Post> postPage = mergePages(postPageFromReceiver, postPageFromRequester, pageable);

        Page<PostWithNameResponseDto> posts = postPage.map(post -> new PostWithNameResponseDto(
            post.getId(),
            post.getUser().getName(),
            post.getTitle(),
            post.getContent(),
            post.getCreatedAt(),
            post.getModifiedAt()
        ));

        //페이지 정보 객체를 생성
        FindPostResponseDto.PageInfo pageInfo = new FindPostResponseDto.PageInfo(
            posts.getNumber() + 1,
            posts.getSize(),
            (int) posts.getTotalElements(),
            posts.getTotalPages()
        );

        return new FindPostResponseDto(posts.getContent(), pageInfo);
    }


    private Page<Post> mergePages(Page<Post> page1, Page<Post> page2, Pageable pageable) {
        List<Post> mergedContent = new ArrayList<>();
        mergedContent.addAll(page1.getContent());
        mergedContent.addAll(page2.getContent());

        int totalElements = (int) (page1.getTotalElements() + page2.getTotalElements());

        return new PageImpl<>(mergedContent, pageable, totalElements);

    }

    //이미 친구 신청 상태인 친구 목록을사
    public DeleteFriendResponseDto deleteFriend(DeleteFriendServiceDto dto) {
        List<Friend> friends = friendRepository.findAllFriendReceivedACCEPTED(dto.getUserId());
        friends.addAll(friendRepository.findAllFriendReceivedACCEPTED(dto.getUserId()));

        Friend targetFriend = friends.stream()
            .filter(friend -> friend.getId().equals(dto.getFriendId())).findFirst()
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 친구를 찾을 수 없습니다.")
            );

        friendRepository.delete(targetFriend);
        return new DeleteFriendResponseDto(true);
    }
}
