package com.hong.chatservice.chat.application;


import com.hong.chatservice.chat.presentation.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    @Transactional
    public void createChat(ChatMessage chatMessage) {

    }

}
