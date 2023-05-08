package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;

@Service
@Slf4j
public class EnterRoomEvent {

    @Autowired
    private SimpMessagingTemplate template;

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String destination = sha.getFirstNativeHeader("destination");
        ChatMessage chatMessage = new ChatMessage(1L, "server", " 누구 입장");

        template.convertAndSend(destination, chatMessage);
    }
}
