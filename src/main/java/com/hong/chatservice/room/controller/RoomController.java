package com.hong.chatservice.room.controller;

import com.hong.chatservice.room.application.RoomResponseDto;
import com.hong.chatservice.room.application.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    /**
     * 방 조회
     */
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity roomDetails(@PathVariable("roomId") Long roomId){
        RoomResponseDto roomResponse = roomService.retrieveRoom(roomId);
        return ResponseEntity.ok(roomResponse);
    }

    /**
     * 방 목록
     */
    @GetMapping("/rooms")
    public ResponseEntity roomList(){
        List<RoomResponseDto> roomResponseDtoList = roomService.retrieveAllRooms();
        return ResponseEntity.ok(roomResponseDtoList);
    }

    /**
     * 방 생성
     */
    @PostMapping("/rooms")
    public ResponseEntity registerRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest) {

        Long roomId = roomService.createRoom(createRoomRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(roomId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * 방 입장
     */
    @PostMapping("/rooms/members")
    public ResponseEntity joinRoom(@RequestBody @Valid JoinRoomRequest joinRoomRequest) {

        roomService.joinRoom(joinRoomRequest);

        return ResponseEntity.ok().build();
    }

}
