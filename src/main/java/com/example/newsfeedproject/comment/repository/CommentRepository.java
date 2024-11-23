package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);
}
