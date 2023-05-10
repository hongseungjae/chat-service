package com.hong.chatservice.room.application;

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
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomResponseDto {

    @NotNull
    private Long id;

    @NotNull
    private String roomName;

    private List<ParticipantResponseDto> participantsInfo = new ArrayList<>();

    @Size(min = 2)
    private int maxHeadCount;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @Builder
    private RoomResponseDto(Long id, String roomName, List<ParticipantResponseDto> participantsInfo, int maxHeadCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.roomName = roomName;
        this.participantsInfo = participantsInfo;
        this.maxHeadCount = maxHeadCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RoomResponseDto toDto(Room room){

        List<ParticipantResponseDto> participantResponseDto = room.getParticipants().stream()
                .map(participant -> ParticipantResponseDto.builder()
                        .memberName(participant.getMember().getMemberName())
                        .roomRole(participant.getRoomRole())
                        .build())
                .collect(Collectors.toList());

        RoomResponseDto build = RoomResponseDto.builder().
                id(room.getId()).
                roomName(room.getRoomName()).
                participantsInfo(participantResponseDto).
                maxHeadCount(room.getMaxHeadCount()).
                createdAt(room.getCreatedAt()).
                updatedAt(room.getUpdatedAt()).build();

        return build;
    }

}
