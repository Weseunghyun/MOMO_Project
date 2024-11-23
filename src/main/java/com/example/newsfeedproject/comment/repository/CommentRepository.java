package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long postId);
}
