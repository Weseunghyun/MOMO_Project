package com.example.newsfeedproject.friend.repository;

import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    // WAITING 상태의 받은 친구 요청 조회
    @Query("select f from User u join u.receivedFriendRequests f where u.id = :id and f.id = :friendId " +
            "and f.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.WAITING")
    Optional<Friend> findAllFriendReceivedWaitingFindByFriendId(@Param("id") Long id, @Param("friendId") Long friendId);

    // ACCEPTED 상태의 받은 친구 요청 조회
    @Query("select f from User u join u.receivedFriendRequests f where u.id = :id and f.id = :friendId and f.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    Optional<Friend> findAllFriendReceivedAcceptedFindByFriendId(@Param("id") Long id, @Param("friendId") Long friendId);

    // ACCEPTED 상태의 보낸 친구 요청 조회
    @Query("select f from User u join u.sentFriendRequests f where u.id = :id and f.id = :friendId and f.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    Optional<Friend> findAllFriendSentAcceptedFindByFriendId(@Param("id") Long id, @Param("friendId") Long friendId);

    // 상태 구분 없이 모든 받은 친구 요청 조회
    @Query("select f from User u join u.receivedFriendRequests f where u.id = :id ")
    List<Friend> findAllFriendReceived(@Param("id") Long id);

    // 상태 구분 없이 모든 보낸 친구 요청 조회
    @Query("select f from User u join u.sentFriendRequests f where u.id = :id ")
    List<Friend> findAllFriendSent(@Param("id") Long id);

    // 보낸 친구 요청중 ACCEPTED 상태인 상대방의 게시글을 Pageable로 가져옴
    @Query(value = "select p from User u join  u.sentFriendRequests sf " +
        "join sf.receiver ru join ru.posts p where u.id = :id " +
        "and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED",
        countQuery =
            "select count(p)from User u join  u.sentFriendRequests sf join sf.receiver ru join ru.posts p"
                +
                " where u.id = :id and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    Page<Post> findFriendPostsFromReceiver(@Param("id") Long id, Pageable pageable);

    // 받은 친구 요청중 ACCEPTED 상태인 상대방의 게시글을 Pageable로 가져옴
    @Query(value = "select p from User u join  u.receivedFriendRequests sf " +
        "join sf.requester ru join ru.posts p where u.id = :id " +
        "and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED",
        countQuery =
            "select count(p)from User u join  u.receivedFriendRequests sf join sf.receiver ru join ru.posts p"
                +
                " where u.id = :id and ru.id != :id and sf.status = com.example.newsfeedproject.friend.entity.Friend.FriendStatus.ACCEPTED")
    Page<Post> findFriendPostsFromRequester(@Param("id") Long id, Pageable pageable);

}
