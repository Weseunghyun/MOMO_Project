package com.example.newsfeedproject.like.controller;

import com.example.newsfeedproject.like.dto.comment.AddCommentLikeResponseDto;
import com.example.newsfeedproject.like.dto.comment.AddCommentLikeServiceDto;
import com.example.newsfeedproject.like.dto.comment.DeleteCommentLikeResponseDto;
import com.example.newsfeedproject.like.dto.comment.DeleteCommentLikeServiceDto;
import com.example.newsfeedproject.like.dto.post.AddPostLikeResponseDto;
import com.example.newsfeedproject.like.dto.post.AddPostLikeServiceDto;
import com.example.newsfeedproject.like.dto.post.DeletePostLikeResponseDto;
import com.example.newsfeedproject.like.dto.post.DeletePostLikeServiceDto;
import com.example.newsfeedproject.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;


    @PostMapping("/posts/{postId}")
    public ResponseEntity<AddPostLikeResponseDto> addPostLike(
            @PathVariable("postId") Long postId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        AddPostLikeServiceDto serviceDto = new AddPostLikeServiceDto(postId, (Long) session.getAttribute("userId"));
        return new ResponseEntity<>(likeService.addPostLike(serviceDto),HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<DeletePostLikeResponseDto> deletePostLike(
            @PathVariable("postId") Long postId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        DeletePostLikeServiceDto serviceDto = new DeletePostLikeServiceDto(postId, (Long) session.getAttribute("userId"));
        return new ResponseEntity<>(likeService.deleteCommentLike(serviceDto),HttpStatus.OK);
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<AddCommentLikeResponseDto> addCommentLike(
            @PathVariable("commentId") Long commentId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        AddCommentLikeServiceDto serviceDto = new AddCommentLikeServiceDto(commentId, (Long) session.getAttribute("userId"));
        return new ResponseEntity<>(likeService.addCommentLike(serviceDto),HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<DeleteCommentLikeResponseDto> deleteCommentLike(
            @PathVariable("commentId") Long commentId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        DeleteCommentLikeServiceDto serviceDto = new DeleteCommentLikeServiceDto(commentId, (Long) session.getAttribute("userId"));
        return new ResponseEntity<>(likeService.deleteCommentLike(serviceDto),HttpStatus.OK);
    }

}