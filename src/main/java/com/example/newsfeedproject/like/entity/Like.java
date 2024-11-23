package com.example.newsfeedproject.like.entity;

import com.example.newsfeedproject.comment.entity.Comment;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
@Table(name = "likes"
        ,uniqueConstraints =
        {@UniqueConstraint( columnNames = {"user_id", "post_id"})
        ,@UniqueConstraint(columnNames = {"user_id", "comment_id"})}
        )
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Like() {}

    public void likePost(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public void likeComment(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }



}
