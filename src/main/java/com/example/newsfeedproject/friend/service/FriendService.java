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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    //친구 요청을 위한 서비스 메서드
    public RequestFriendResponseDto requestFriend(RequestFriendServiceDto dto) {
        // 상대방 정보가 존재하는지 확인
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "받는 사람의 유저를 찾을 수 없습니다.")
                );
        // 자신의 정보가 존재하는지 확인
        User requester = userRepository.findById(dto.getRequesterId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "보내는 사람의 유저를 찾을 수 없습니다.")
                );
        // 자신에게 친구 요청을 했는지 확인
        if (receiver.equals(requester)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신에게 친구 요청을 할 수 없습니다");
        }
        // 자신의 친구 테이블을 모두 조회 자신에게 연결된 테이블이 두 종류 이므로 두개의 테이블을 하나의 list로 합친다.
        List<Friend> friends = friendRepository.findAllFriendSent(dto.getRequesterId());
        friends.addAll(friendRepository.findAllFriendReceived(dto.getRequesterId()));

        // 이미 친구 요청을 하였거나 이미 친구상태인 경우 확인
        for (Friend friend : friends) {
            if (friend.getRequester().equals(receiver) || friend.getReceiver().equals(receiver)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 대상에게 친구 요청을 하였습니다.");
            }
        }
        // 예외사항을 모두 통과하면 새로운 friend 객체를 생성하고 저장한다. default status가 WAITING이 된다.
        Friend friend = new Friend(requester, receiver);
        friendRepository.save(friend);

        return new RequestFriendResponseDto(true);
    }

    public AcceptFriendResponseDto acceptFriend(AcceptFriendServiceDto dto) {
        // 수락할 friend를 조회
        Optional<Friend> requestFriend = friendRepository.findAllFriendReceivedWaitingFindByFriendId(dto.getUserId(), dto.getFriendId());
        // 부합하는 friend가 존재하지 않으면 예외를 던짐
        if (requestFriend.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "친구 요청을 찾지 못하였습니다.");
        }
        // friend의 status를 ACCEPTED로 변경하고 저장
        requestFriend.get().accept();
        friendRepository.save(requestFriend.get());
        return new AcceptFriendResponseDto(true);
    }

    public RejectFriendResponseDto rejectFriend(RejectFriendServiceDto dto) {
        // 거절할 friend를 조회
        Optional<Friend> requestFriend = friendRepository.findAllFriendReceivedWaitingFindByFriendId(dto.getUserId(), dto.getFriendId());
        if (requestFriend.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "친구 요청을 찾지 못하였습니다.");
        }

        // friend 삭제
        friendRepository.delete(requestFriend.get());

        return new RejectFriendResponseDto(true);
    }

    // 친구 게시글 조회 메서드
    public FindPostResponseDto findPost(FindPostServiceDto dto) {
        // 조회할 게시글 페이지 설정
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by("p.createdAt").descending());
        // 친구의 게시글을 모두 Page 구조로 조회한다.
        Page<Post> postPageFromReceiver = friendRepository.findFriendPostsFromReceiver(dto.getUserId(), pageable);
        Page<Post> postPageFromRequester = friendRepository.findFriendPostsFromRequester(dto.getUserId(), pageable);
        // page를 하나로 합친다.
        Page<Post> postPage = mergePages(postPageFromReceiver, postPageFromRequester, pageable);
        // post를 FindPostResponseDto의 멤버 변수에 매핑한다.
        Page<PostWithNameResponseDto> posts = postPage.map(post -> new PostWithNameResponseDto(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        ));

        // 페이지 설정을 FindPostResponseDto에 저장
        FindPostResponseDto.PageInfo pageInfo = new FindPostResponseDto.PageInfo(
                posts.getNumber() + 1,
                posts.getSize(),
                (int) posts.getTotalElements(),
                posts.getTotalPages()
        );

        return new FindPostResponseDto(posts.getContent(), pageInfo);
    }

    // 두개의 Page 자료를 하나로 합치기 위한 메서드
    private Page<Post> mergePages(Page<Post> page1, Page<Post> page2, Pageable pageable) {
        List<Post> mergedContent = new ArrayList<>();
        mergedContent.addAll(page1.getContent());
        mergedContent.addAll(page2.getContent());

        int totalElements = (int) (page1.getTotalElements() + page2.getTotalElements());

        return new PageImpl<>(mergedContent, pageable, totalElements);

    }

    // 친구를 삭제하는 메서드
    public DeleteFriendResponseDto deleteFriend(DeleteFriendServiceDto dto) {
        // friedId를 통해 ACCEPTED 상태의 친구 조회
        Optional<Friend> deleteFriend = friendRepository.findAllFriendReceivedAcceptedFindByFriendId(dto.getUserId(), dto.getFriendId());
        if (deleteFriend.isEmpty()) {
            deleteFriend = friendRepository.findAllFriendSentAcceptedFindByFriendId(dto.getUserId(), dto.getFriendId());
        }
        // 부합하는 friend가 존재하지 않으면 예외를 던진다.
        if (deleteFriend.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 친구를 찾을 수 없습니다");
        }
        // friend를 삭제한다
        friendRepository.delete(deleteFriend.get());

        return new DeleteFriendResponseDto(true);
    }
}
