package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.MessageType;
import com.hong.chatservice.chat.presentation.ServerMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class LeaveServerMessage extends ServerMessage {

    private String leavedUserName;
    @Builder
    public LeaveServerMessage(String sourceName, @NotEmpty @Size(min = 1, max = 300) String content, MessageType messageType, String leavedUserName) {
        super(sourceName, content, messageType);
        this.leavedUserName = leavedUserName;
    }
}
