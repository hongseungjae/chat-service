package com.hong.chatservice.room.controller;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotBlank
    String roomName;

    @Min(2) @Max(50)
    int maxHeadCount;

    @NotNull
    Long memberId;

}
