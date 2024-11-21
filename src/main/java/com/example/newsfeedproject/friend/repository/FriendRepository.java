package com.example.newsfeedproject.friend.repository;

import com.example.newsfeedproject.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("select f from User u join u.receivedFriendRequests f where u.id = :id")
    List<Friend> findAllFriendRequests(@Param("id") Long id);
}
