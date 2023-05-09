package com.hong.chatservice.room.application;

import com.hong.chatservice.participant.domain.Participant;
import com.hong.chatservice.room.domain.Room;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomResponseDto {

    @NotNull
    private Long id;

    @NotNull
    private String roomName;

    private List<Participant> participants = new ArrayList<>();

    @Size(min = 2)
    private int maxHeadCount;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @Builder
    private RoomResponseDto(Long id, String roomName, List<Participant> participants, int maxHeadCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.roomName = roomName;
        this.participants = participants;
        this.maxHeadCount = maxHeadCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RoomResponseDto toDto(Room room){
        return RoomResponseDto.builder().
                id(room.getId()).
                roomName(room.getRoomName()).
                participants(room.getParticipants()).
                maxHeadCount(room.getMaxHeadCount()).
                createdAt(room.getCreatedAt()).
                updatedAt(room.getUpdatedAt()).build();
    }
}
