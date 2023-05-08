package com.hong.chatservice.room.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotBlank
    String roomName;

    @Size(min = 2, max = 50)
    int maxHeadCount;

    @NotNull
    Long memberId;

}
