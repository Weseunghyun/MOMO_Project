package com.example.newsfeedproject.friend.entity;

import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 친구 관계 고유 식별자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status = FriendStatus.WAITING; // 친구 요청 대기 상태가 초기 상태

    @Column(nullable = false)
    private LocalDateTime requestedAt = LocalDateTime.now(); // 요청 일자

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester; // 친구 요청을 보낸 사용자

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // 친구 요청을 받은 사용자

    // Enum 클래스 정의
    public enum FriendStatus {
        WAITING, ACCEPTED
    }
}
