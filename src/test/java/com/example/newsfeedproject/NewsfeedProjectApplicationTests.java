package com.example.newsfeedproject;

import com.example.newsfeedproject.friend.dto.accept.AcceptFriendResponseDto;
import com.example.newsfeedproject.friend.dto.accept.AcceptFriendServiceDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendResponseDto;
import com.example.newsfeedproject.friend.dto.request.RequestFriendServiceDto;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.friend.repository.FriendRepository;
import com.example.newsfeedproject.friend.service.FriendService;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    // friendservice 친구신청 테스트
    @Test
    void RequestFriendTest()throws Exception {
        //given
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        RequestFriendServiceDto dto = new RequestFriendServiceDto(receiver.getId(),requester.getId());
        //when
        RequestFriendResponseDto responseDto = friendService.RequestFriend(dto);
        Optional<Friend> friend = friendRepository.findById(1L);
        //then
        assertThat(responseDto).isNotNull();
        assertThat(friend.isPresent()).isTrue();
    }
    @Test
    void AcceptFriendTest()throws Exception {
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        RequestFriendServiceDto dto = new RequestFriendServiceDto(receiver.getId(),requester.getId());
        //when
        RequestFriendResponseDto requestFriendResponseDto = friendService.RequestFriend(dto);
        Optional<Friend> friend = friendRepository.findById(1L);
        //then
        AcceptFriendServiceDto acceptFriendServiceDto = new AcceptFriendServiceDto(receiver.getId(),friend.get().getId());
        AcceptFriendResponseDto acceptFriendResponseDto = friendService.AcceptFriend(acceptFriendServiceDto);
        Optional<Friend> acceptFriend = friendRepository.findById(1L);
        assertThat(acceptFriendResponseDto).isNotNull();
        assertThat(acceptFriend.get().getStatus() == Friend.FriendStatus.ACCEPTED).isTrue();
    }

}
