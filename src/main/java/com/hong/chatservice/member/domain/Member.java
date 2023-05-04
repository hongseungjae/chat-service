package com.hong.chatservice.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String memberName; // 회원 아이디
    private String password;
    private String name; // 이름
    private String email;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    public Member(String memberName) {
        this.memberName = memberName;
    }

    public Member(String memberName, String password) {
        this.memberName = memberName;
        this.password = password;
    }

    @Builder
    public Member(String memberName, String password, String name, String email) {
        this.memberName = memberName;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

}
