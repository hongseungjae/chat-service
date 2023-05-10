package com.hong.chatservice.room.application;

import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import com.hong.chatservice.room.controller.CreateRoomRequest;
import com.hong.chatservice.room.controller.JoinRoomRequest;
import com.hong.chatservice.room.domain.Room;
import com.hong.chatservice.room.infrastructure.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;


    public RoomResponseDto retrieveRoom(Long roomId) {
        Room finedRoom = findDetailRoom(roomId);
        RoomResponseDto roomResponseDto = RoomResponseDto.toDto(finedRoom);
        return roomResponseDto;
    }

    public List<RoomResponseDto> retrieveAllRooms() {
        List<Room> allRooms = roomRepository.findRooms();

        List<RoomResponseDto> roomResponseDtoList = allRooms.stream()
                .map(room -> RoomResponseDto.toDto(room))
                .collect(Collectors.toList());

        return roomResponseDtoList;
    }

    @Transactional
    public Long createRoom(CreateRoomRequest request){

        Member member = findMember(request.getMemberId());
        Room createRoom = Room.builder()
                .roomName(request.getRoomName())
                .maxHeadCount(request.getMaxHeadCount())
                .admin(member).build();

        return roomRepository.save(createRoom).getId();

    }

    @Transactional
    public void joinRoom(JoinRoomRequest request){
        Room finedRoom = findRoom(request.getRoomId());
        Member member = findMember(request.getJoinMemberId());
        finedRoom.addParticipant(member);
    }

    private Room findRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(RuntimeException::new);
    }

    private Room findDetailRoom(Long roomId) {
        return roomRepository.findDetails(roomId)
                .orElseThrow(RuntimeException::new);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);
    }

}
