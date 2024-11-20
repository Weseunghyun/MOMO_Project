package com.example.newsfeedproject.post.controller;

import com.example.newsfeedproject.post.dto.PostPageResponseDto;
import com.example.newsfeedproject.post.dto.PostRequestDto;
import com.example.newsfeedproject.post.dto.PostResponseDto;
import com.example.newsfeedproject.post.dto.PostUpdateRequestDto;
import com.example.newsfeedproject.post.dto.PostUpdateResponseDto;
import com.example.newsfeedproject.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<PostPageResponseDto> getPostsPaginated(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ){

        PostPageResponseDto pageResponseDto = postService.getPostsPaginated(page - 1, size);

        return new ResponseEntity<>(pageResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost(
        HttpServletRequest request,
        @PathVariable Long postId,
        @RequestBody PostUpdateRequestDto requestDto
    ){
        PostUpdateResponseDto responseDto = postService.updatePost(
            request,
            postId,
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getPassword()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
