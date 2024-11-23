package com.example.newsfeedproject.comment.controller;

import com.example.newsfeedproject.comment.dto.CommentRequestDto;
import com.example.newsfeedproject.comment.dto.CommentResponseDto;
import com.example.newsfeedproject.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
@Getter

public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            HttpServletRequest request,
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto
    ) {

        CommentResponseDto responseDto = commentService.createComment(
                request,
                postId,
                requestDto.getContent()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
