package com.example.newsfeedproject;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendRequestDto;
import com.example.newsfeedproject.friend.dto.delete.DeleteFriendServiceDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostResponseDto;
import com.example.newsfeedproject.friend.dto.findpost.FindPostServiceDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendResponseDto;
import com.example.newsfeedproject.friend.dto.reject.RejectFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.repository.FriendRepository;
import com.example.newsfeedproject.friend.service.FriendService;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NewsfeedProjectApplicationTests {

    @Autowired
    private  FriendService friendService;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    // friendservice 친구신청 테스트
    @Test
    void requestFriendTest()throws Exception {
        //given
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        RequestFriendServiceDto dto = new RequestFriendServiceDto(receiver.getId(),requester.getId());
        //when
        RequestFriendResponseDto responseDto = friendService.requestFriend(dto);
        Optional<Friend> friend = friendRepository.findById(1L);
        //then
        assertThat(responseDto).isNotNull();
        assertThat(friend.isPresent()).isTrue();
    }
    // 친구 수락 테스트
    @Test
    void acceptFriendTest()throws Exception {
        //given
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        RequestFriendServiceDto requestFriendServiceDto = new RequestFriendServiceDto(receiver.getId(),requester.getId());
        friendService.requestFriend(requestFriendServiceDto);
        Optional<Friend> friend = friendRepository.findById(1L);
        AcceptFriendServiceDto acceptFriendServiceDto = new AcceptFriendServiceDto(receiver.getId(),friend.get().getId());
        //when
        AcceptFriendResponseDto acceptFriendResponseDto = friendService.acceptFriend(acceptFriendServiceDto);
        //then
        Optional<Friend> acceptFriend = friendRepository.findById(1L);
        assertThat(acceptFriendResponseDto).isNotNull();
        assertThat(acceptFriend.get().getStatus() == Friend.FriendStatus.ACCEPTED).isTrue();
    }
    // 친구 거절 테스트
    @Test
    void rejectFriendTest()throws Exception {
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        Friend friend = new Friend(requester,receiver);
        friendRepository.save(friend);

        Optional<Friend> friendexist = friendRepository.findAll().stream().findFirst();
        RejectFriendServiceDto rejectFriendServiceDto = new RejectFriendServiceDto(friendexist.get().getId(), friendexist.get().getReceiver().getId());
        //when
        RejectFriendResponseDto rejectFriendResponseDto = friendService.rejectFriend(rejectFriendServiceDto);

        //then
        Optional<Friend> rejectFriend = friendRepository.findById(friendexist.get().getId());
     
        assertThat(rejectFriendResponseDto).isNotNull();
        assertThat(rejectFriend).isEmpty();
    }
    // 친구 게시글 조회 테스트
    @Test
    void findPostTest()throws Exception {
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        postRepository.save(new Post("title","content",requester));
        postRepository.save(new Post("title2","content2",receiver));
        Friend friend = new Friend(requester,receiver);
        friendRepository.save(friend);
        AcceptFriendServiceDto acceptFriendServiceDto = new AcceptFriendServiceDto(receiver.getId(),friend.getId());
        friendService.acceptFriend(acceptFriendServiceDto);
        FindPostServiceDto findPostServiceDto = new FindPostServiceDto(receiver.getId());
        //when
        FindPostResponseDto posts = friendService.findPost(findPostServiceDto);
        // 삭제를 테스트 하기 위해 삭제한 후에 캐시를 초기화

        //then
        assertThat(posts.getPosts().get(0).getId().equals(1L)).isTrue();

    }
    // 친구 삭제 테스트
    @Test
    void deleteFriend()throws Exception {
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        Friend friend = new Friend(requester,receiver);
        friendRepository.save(friend);

        Optional<Friend> friendexist = friendRepository.findAll().stream().findFirst();
        DeleteFriendServiceDto deleteFriendServiceDto = new DeleteFriendServiceDto(receiver.getId(),friendexist.get().getId());
        //when
        friendService.deleteFriend(deleteFriendServiceDto);

        //then
        Optional<Friend> deletedFriend = friendRepository.findAll().stream().findFirst();

        assertThat(deletedFriend).isEmpty();

    }
}
