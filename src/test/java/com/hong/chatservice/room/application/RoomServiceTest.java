package com.hong.chatservice.room.application;

import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import com.hong.chatservice.room.controller.CreateRoomRequest;
import com.hong.chatservice.room.domain.Room;
import com.hong.chatservice.room.infrastructure.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void retrieveRoom() {
    }

    @Test
    void retrieveAllRooms() {
    }

    @Test
    @DisplayName("방을 생성하고 조회한다.")
    void createRoom() {
        Member admin = new Member("HongSeungJae");

        memberRepository.save(admin);

        String roomName = "first";
        int headCount = 5;

        CreateRoomRequest createRoomRequest = new CreateRoomRequest(roomName, headCount, admin.getId());
        Long roomId = roomService.createRoom(createRoomRequest);

        Room room = roomRepository.findById(roomId).get();

        Assertions.assertEquals(roomName, room.getRoomName());

    }

    @Test
    void joinRoom() {
    }
}