package com.example.newsfeedproject.comment.entity;

import com.example.newsfeedproject.common.entity.TimeBaseEntity;
import com.example.newsfeedproject.like.entity.Like;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Setter
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 (ManyToOne 관계)

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public String getName() {
        return user.getName();
    }

    public Comment(String content) {
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

    public Long getUserId(){
        return user.getId();
    }

    public Long getPostOwnerId(){
        return post.getUser().getId();
    }
}
