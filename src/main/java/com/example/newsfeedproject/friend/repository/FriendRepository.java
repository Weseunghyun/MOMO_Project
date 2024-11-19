package com.example.newsfeedproject.friend.repository;

import com.example.newsfeedproject.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
