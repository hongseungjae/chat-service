package com.hong.chatservice.participant.Service;

import com.hong.chatservice.participant.domain.Participant;
import com.hong.chatservice.participant.infrastructure.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public void retrieveRoom(Long roomId) {
        /*Room finedRoom = findDetailRoom(roomId);
        RoomResponseDto roomResponseDto = RoomResponseDto.toDto(finedRoom);
        return roomResponseDto;*/
    }

    public List<String> retrieveParticipants(Long roomId) {
        List<Participant> participants = participantRepository.findParticipantByRoomId(roomId);
        for (Participant participant : participants) {
            System.out.println("participant = " + participant.getMember().getMemberName());
        }

        List<String> memberNames = new ArrayList<>();
        participants.stream().forEach(participant ->
                memberNames.add(participant.getMember().getMemberName()));
        return memberNames;

        /*List<RoomResponseDto> roomResponseDtoList = allRooms.stream()
                .map(room -> RoomResponseDto.toDto(room))
                .collect(Collectors.toList());*/

    }


}
