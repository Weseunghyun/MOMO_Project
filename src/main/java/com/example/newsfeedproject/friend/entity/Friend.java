package com.example.newsfeedproject.friend.entity;

import com.example.newsfeedproject.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "friend")
@EntityListeners(AuditingEntityListener.class)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 친구 관계 고유 식별자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status = FriendStatus.WAITING; // 친구 요청 대기 상태가 초기 상태

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestedAt; // 요청 일자
    //
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester; // 친구 요청을 보낸 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // 친구 요청을 받은 사용자

    public Friend() {}

    public Friend(User requester, User receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }
    public void accept() {
        status = FriendStatus.ACCEPTED;
    }

    // Enum 클래스 정의
    public enum FriendStatus {
        WAITING, ACCEPTED
    }
}
