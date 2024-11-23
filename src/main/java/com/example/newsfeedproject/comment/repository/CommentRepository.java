package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    default Comment findByIdOrElseThrow(Long id) {
        return findById(id).
            orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다.")
            );
    }
}
