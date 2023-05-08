package com.hong.chatservice.room.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinRoomRequest {

    @NotNull
    Long roomId;

    @NotNull
    Long joinMemberId;

}
