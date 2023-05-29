package com.hong.chatservice.room.domain;


import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.participant.domain.Participant;
import com.hong.chatservice.participant.domain.RoomRole;
import com.hong.chatservice.setting.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @NotNull
    private String roomName;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @Min(2) @Max(50)
    private int maxHeadCount;

    public void checkEnoughHeadCount() {
        if (!(participants.size() < maxHeadCount)) {
            throw new RuntimeException();
        }
    }

    public void addParticipant(Member member) {
        checkEnoughHeadCount();
        checkParticipant(member.getId());
        participants.add(new Participant(member, this, RoomRole.room_user));
    }

    public void addParticipant(Member member, RoomRole roomRole) {
        checkEnoughHeadCount();
        checkParticipant(member.getId());
        participants.add(new Participant(member, this, roomRole));
    }

    public void removeParticipant(Long memberId) {
        Participant participant = findParticipant(memberId);
        participants.remove(participant);
    }

    public Participant findParticipant(Long findMemberId) {
        return participants.stream()
                .filter(participant -> participant.getMember().getId().equals(findMemberId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void checkParticipant(Long findMemberId) {
        participants.stream()
                .filter(participant -> participant.getMember().getId().equals(findMemberId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }


    @Builder
    public Room(String roomName, int maxHeadCount, Member admin) {
        this.roomName = roomName;
        this.maxHeadCount = maxHeadCount;

        this.participants.add(new Participant(admin, this, RoomRole.room_admin));
    }
}
