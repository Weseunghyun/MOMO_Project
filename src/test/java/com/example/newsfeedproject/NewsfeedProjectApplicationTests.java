package com.example.newsfeedproject;

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
        String link = "asd";
        User requester = new User("송정학","asd@naver.com","asd","1234");
        User receiver = new User("송정학","asdf@naver.com","asd","1234");
        userRepository.save(requester);
        userRepository.save(receiver);
        RequestFriendServiceDto dto = new RequestFriendServiceDto(requester.getId(),receiver.getId());
        //when
        RequestFriendResponseDto ResponseDto = friendService.RequestFriend(dto);
        Optional<Friend> friend = friendRepository.findById(1L);
        //then
        assertThat(ResponseDto).isNotNull();
        assertThat(friend.isPresent()).isTrue();


    }

}
