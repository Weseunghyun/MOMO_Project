package com.example.newsfeedproject.like.repository;

import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.like.entity.Like;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface LikeRepository extends JpaRepository<Like, Long> {
    default void deleteLike(Like like) {
        delete(like);
    }
    default void addLike(Like like) {
        save(like);
    }

    List<Like> findAllByUser(User user);

    Like findAllByUserAndPost(User user, Post post);

    Like findAllByUserAndComment(User user, Comment comment);
}
