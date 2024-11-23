package com.example.newsfeedproject.like.service;

import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.like.dto.comment.AddCommentLikeResponseDto;
import com.example.newsfeedproject.like.dto.comment.AddCommentLikeServiceDto;
import com.example.newsfeedproject.like.dto.comment.DeleteCommentLikeResponseDto;
import com.example.newsfeedproject.like.dto.comment.DeleteCommentLikeServiceDto;
import com.example.newsfeedproject.like.dto.post.AddPostLikeResponseDto;
import com.example.newsfeedproject.like.dto.post.AddPostLikeServiceDto;
import com.example.newsfeedproject.like.dto.post.DeletePostLikeResponseDto;
import com.example.newsfeedproject.like.dto.post.DeletePostLikeServiceDto;
import com.example.newsfeedproject.like.entity.Like;
import com.example.newsfeedproject.like.repository.LikeRepository;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public AddPostLikeResponseDto addPostLike(AddPostLikeServiceDto dto){
        Like like = new Like();
        User user = userRepository.findByIdOrElseThrow(dto.getUserId());
        Post post = postRepository.findByIdOrElseThrow(dto.getPostId());
        if(post.getUser().equals(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신의 게시글에 좋아요 할 수 없습니다");
        }
        like.likePost(user,post);
        user.getLikes().add(like);
        post.getLikes().add(like);
        try {
            likeRepository.save(like);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"좋아요 추가 실패");
        }
        return new AddPostLikeResponseDto(true);
    }

    public DeletePostLikeResponseDto deleteCommentLike(DeletePostLikeServiceDto dto){

        User user = userRepository.findByIdOrElseThrow(dto.getUserId());
        Post post = postRepository.findByIdOrElseThrow(dto.getPostId());
        Like like = likeRepository.findAllByUserAndPost(user,post);
        user.getLikes().remove(like);
        post.getLikes().remove(like);
        likeRepository.delete(like);
        return new DeletePostLikeResponseDto(true);
    }

    public AddCommentLikeResponseDto addCommentLike(AddCommentLikeServiceDto dto){
        Like like = new Like();
        User user = userRepository.findByIdOrElseThrow(dto.getUserId());
        Comment comment = commentRepository.findByIdOrElseThrow(dto.getCommentId());
        if(comment.getUser().equals(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신의 게시글에 좋아요 할 수 없습니다");
        }
        like.likeComment(user,comment);
        user.getLikes().add(like);
        comment.getLikes().add(like);
        try {
            likeRepository.save(like);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"좋아요 추가 실패");
        }
        return new AddCommentLikeResponseDto(true);
    }

    public DeleteCommentLikeResponseDto deleteCommentLike(DeleteCommentLikeServiceDto dto){

        User user = userRepository.findByIdOrElseThrow(dto.getUserId());
        Comment comment = commentRepository.findByIdOrElseThrow(dto.getCommentId());
        Like like = likeRepository.findAllByUserAndComment(user,comment);
        user.getLikes().remove(like);
        comment.getLikes().remove(like);
        likeRepository.delete(like);
        return new DeleteCommentLikeResponseDto(true);
    }

}
