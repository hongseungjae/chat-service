package com.hong.chatservice.chat.presentation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServerMessage {

    private String sourceName;
    @NotEmpty
    @Size(min = 1, max = 300)
    private String content;
    private MessageType messageType;
}