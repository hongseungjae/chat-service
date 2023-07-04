package com.hong.chatservice.chat.presentation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Builder
    public ServerMessage(String sourceName, String content, MessageType messageType) {
        this.sourceName = sourceName;
        this.content = content;
        this.messageType = messageType;
    }
}