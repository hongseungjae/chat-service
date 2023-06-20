package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.MessageType;
import com.hong.chatservice.chat.presentation.ServerMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
public class JoinServerMessage extends ServerMessage {
    private ArrayList<String> userNames = new ArrayList<>();

    @Builder
    public JoinServerMessage(String sourceName, @NotEmpty @Size(min = 1, max = 300) String content, MessageType messageType, ArrayList<String> userNames) {
        super(sourceName, content, messageType);
        this.userNames = userNames;
    }
}
