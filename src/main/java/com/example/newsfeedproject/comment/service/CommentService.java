package com.example.newsfeedproject.comment.service;

import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 작성
     */
    public CommentResponseDto createComment(HttpServletRequest request, String comment) {
        HttpSession session = request.getSession(false);

        // 세션을 통해 게시글 생성하려는 게시판 id 가져오기
        Long postId = (Long) session.getAttribute("postId");

        // 세션을 통해 가져온 게시판 id로 해당 유저 객체 찾고 comment 객체 생성
        Post postUser = postRepository.findByIdOrElseThrow(postId);
        Comment savedComment = commentRepository.save(new Comment(comment, postUser.getId()));

        return new CommentResponseDto(
                savedComment.getId(),
                postUser.getUser().getName(),
                savedComment.getComment(),
                savedComment.getCreatedAt()
        );
    }
}
