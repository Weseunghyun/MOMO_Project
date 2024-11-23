package com.example.newsfeedproject.comment.service;

import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.dto.UpdateCommentResponseDto;
import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.common.config.PasswordEncoder;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 댓글 작성
     */
    public CommentResponseDto createComment(HttpServletRequest request, Long postId,
        String content) {
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

    public List<CommentResponseDto> getComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        for (Comment comment : comments) {
            commentResponseDtos.add(new CommentResponseDto(
                comment.getId(),
                comment.getUser().getName(),
                comment.getContent(),
                comment.getCreatedAt()
            ));
        }

        return commentResponseDtos;
    }

    public UpdateCommentResponseDto updateComment(
        Long commentId,
        String content,
        String password,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);

        if (!userId.equals(findComment.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 작성한 댓글만 수정할 수 있습니다.");
        }

        if (!passwordEncoder.matches(password, findComment.getUser().getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다");
        }

        findComment.update(content);

        Comment updateComment = commentRepository.save(findComment);

        return new UpdateCommentResponseDto(
            updateComment.getId(),
            updateComment.getUser().getName(),
            updateComment.getContent(),
            updateComment.getModifiedAt()
        );
    }

    public void deleteComment(Long commentId, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);

        //댓글이 달린 게시글의 유저나 댓글을 직접 단 유저를 제외하고는 댓글을 삭제할 수 없도록 함
        if (!userId.equals(findComment.getUserId()) && !userId.equals(findComment.getPostOwnerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글의 주인이나 댓글의 주인만 삭제할 수 있습니다");
        }

        commentRepository.deleteById(commentId);
    }
}
