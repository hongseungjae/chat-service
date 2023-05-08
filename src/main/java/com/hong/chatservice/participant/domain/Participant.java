package com.hong.chatservice.participant.domain;

import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.room.domain.Room;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Participant {

    @Id
    @GeneratedValue
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    private RoomRole roomRole;

    public Participant(Member member, Room room, RoomRole roomRole) {
        this.member = member;
        this.room = room;
        this.roomRole = roomRole;
    }
}
