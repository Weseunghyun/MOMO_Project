package com.example.newsfeedproject.post.controller;

import com.example.newsfeedproject.post.dto.PostRequestDto;
import com.example.newsfeedproject.post.dto.PostResponseDto;
import com.example.newsfeedproject.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
        HttpServletRequest request,
        @RequestBody PostRequestDto requestDto
    ){

        PostResponseDto responseDto = postService.createPost(request, requestDto.getTitle(), requestDto.getContent());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
