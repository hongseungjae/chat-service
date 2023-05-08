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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createRoom(CreateRoomRequest request){

        Member member = findMember(request.getMemberId());
        Room createRoom = Room.builder()
                .roomName(request.getRoomName())
                .maxHeadCount(request.getMaxHeadCount())
                .admin(member).build();

        roomRepository.save(createRoom);

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

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);
    }

}
