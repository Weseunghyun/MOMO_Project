package com.example.newsfeedproject.friend.service;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendResponseDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendServiceDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostResponseDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostServiceDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendResponseDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendRequestDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.repository.FriendRepository;
import com.example.newsfeedproject.post.dto.PostPageResponseDto;
import com.example.newsfeedproject.post.dto.PostWithNameResponseDto;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if (receiver.equals(requester)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신에게 친구 요청을 할 수 없습니다");
        }
        List<Friend> friends = friendRepository.findAllFriendSentRequests(dto.getRequesterId());
        friends.addAll(friendRepository.findAllFriendReceivedRequest(dto.getRequesterId()));
        // 이미 친구 요청을 하였거나 이미 친구상태인 경우 예외 발생
        for(Friend friend : friends) {
            if(friend.getRequester().equals(receiver) || friend.getReceiver().equals(receiver)) {
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 대상에게 친구 요청을 하였습니다.");
            }
        }
        Friend friend = new Friend(requester, receiver);
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
        // 만약 자신이 보낸 요청은 안보임
        Friend acceptableFriend = accepter.getReceivedFriendRequests().stream().filter(friend -> friend.getId().equals(dto.getFriendId()))
                .filter(friend -> friend.getStatus().equals(Friend.FriendStatus.WAITING)).findFirst()
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "수락할 친구를 찾을 수 없습니다.")
                );
        acceptableFriend.accept();
        friendRepository.save(acceptableFriend);
        return new AcceptFriendResponseDto(true);
    }

    public RejectFriendResponseDto rejectFriend(RejectFriendServiceDto dto) {
        // 자신에게 요청이 온 waitting 상태의 friend를 가져옴
        List<Friend> requestFriends = friendRepository.findAllFriendReceivedRequest(dto.getUserId());
        Friend requestFriend = requestFriends.stream().filter(friend -> friend.getId().equals(dto.getFriendId()))
                .filter(friend -> friend.getStatus().equals(Friend.FriendStatus.WAITING)).findFirst()
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "거절할 요청을 찾을 수 없습니다.")
                );
        // waitting 상태의 friend 삭제
        friendRepository.delete(requestFriend);

        return new RejectFriendResponseDto(true);
    }


    public List<FindPostResponseDto> findPost(FindPostServiceDto dto) {
        // 자신이 요청을 받거나 준 friend중 accepted 상태만 가져옴
        List<Friend> friends = friendRepository.findAllFriendReceivedRequest(dto.getUserId())
                .stream().filter(friend -> friend.getStatus().equals(Friend.FriendStatus.ACCEPTED)).collect(Collectors.toList());
        friends.addAll(friendRepository.findAllFriendSentRequests(dto.getUserId())
                .stream().filter(friend -> friend.getStatus().equals(Friend.FriendStatus.ACCEPTED)).toList());
        List<Post> posts = new ArrayList<>();

        for(Friend friend : friends) {
            // 친구 관계인 상대를 찾기 위해 자기의 id와 receiverId를 비교하여 같은경우 requesterId가 상대임
            Long targetId = Objects.equals(friend.getReceiver().getId(), dto.getUserId()) ?  friend.getRequester().getId() : friend.getReceiver().getId();
            User targetUser = userRepository.findByIdOrElseThrow(targetId);
            // 친구의 포스트들을 모두 posts에 넣음
            posts.addAll(targetUser.getPosts());
        }
        // post를 날짜순으로 내림차순
        posts.sort(Comparator.comparing(Post::getCreatedAt,Comparator.reverseOrder()));
        long postCount = posts.size();
        if (postCount < (long) dto.getSize() *(dto.getPage()-1)) {
            return null;
        }
        // list를 사용해 필요한 객체를 저장
        List<FindPostResponseDto> page = new ArrayList<>();
        for(int i = dto.getSize() *(dto.getPage()-1); i < postCount; i++ ) {
            Post post = posts.get(i);
            page.add(new FindPostResponseDto(
                    post.getId(),
                    post.getUser().getName(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt()

            ));
            if(page.size() == dto.getSize()){
                break;
            }
        }


        return page;
    }

    public DeleteFriendResponseDto deleteFriend(DeleteFriendServiceDto dto) {
        List<Friend> friends = friendRepository.findAllFriendSentRequests(dto.getUserId());
        friends.addAll(friendRepository.findAllFriendReceivedRequest(dto.getUserId())
                .stream().filter(friend -> friend.getStatus().equals(Friend.FriendStatus.ACCEPTED)).toList());
        Friend targetFriend = friends.stream().filter(friend -> friend.getId().equals(dto.getFriendId()))
                .filter(friend -> friend.getStatus().equals(Friend.FriendStatus.ACCEPTED)).findFirst()
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 친구를 찾을 수 없습니다.")
                );
        friendRepository.delete(targetFriend);
        return new DeleteFriendResponseDto(true);
    }
}
