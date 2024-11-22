package com.example.newsfeedproject.user.entity;

import com.example.newsfeedproject.common.entity.TimeBaseEntity;
import com.example.newsfeedproject.friend.entity.Friend;
import com.example.newsfeedproject.post.entity.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 고유 식별자

    @Column(nullable = false)
    private String name; // 유저명

    @Column(nullable = false, unique = true)
    private String email; // 유저 이메일

    private String profileImageUrl; // 유저 프로필 이미지 링크

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private Boolean isDeleted = false; // 1인 경우 true, 0인 경우 false

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 사용자가 작성한 게시글

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> sentFriendRequests = new ArrayList<>(); // 내가 보낸 친구 요청

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> receivedFriendRequests = new ArrayList<>(); // 내가 받은 친구 요청

    public User() {
    }

    public User(String name, String email, String profileImageUrl, String password) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.password = password;
    }

}
