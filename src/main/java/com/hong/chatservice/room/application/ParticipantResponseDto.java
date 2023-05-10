package com.hong.chatservice.room.application;

import com.hong.chatservice.participant.domain.RoomRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantResponseDto {
    private String memberName;
    private RoomRole roomRole;

    @Builder
    public ParticipantResponseDto(String memberName, RoomRole roomRole) {
        this.memberName = memberName;
        this.roomRole = roomRole;
    }
}
