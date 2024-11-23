package com.example.newsfeedproject.comment.service;

import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.results.complete.CompleteFetchBuilderEntityValuedModelPart;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성
     */
    public CommentResponseDto createComment(HttpServletRequest request, Long postId, String content) {
        HttpSession session = request.getSession(false);

        // 세션을 통해 로그인한 사용자의 id 가져옴
        Long userId = (Long) session.getAttribute("userId");
        User findUser = userRepository.findByIdOrElseThrow(userId);

        // 세션을 통해 가져온 게시판 id로 해당 유저 객체 찾고 comment 객체 생성
        Post findPost = postRepository.findByIdOrElseThrow(postId);

        Comment comment = new Comment(content);

        comment.setPost(findPost);
        comment.setUser(findUser);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(
                savedComment.getId(),
                findUser.getName(),
                savedComment.getContent(),
                savedComment.getCreatedAt()
        );
    }
}
