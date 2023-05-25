package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.ServerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;


@Service
@Slf4j
public class EnterRoomEvent {

    @Autowired
    private SimpMessagingTemplate template;

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String destination = sha.getFirstNativeHeader(StompHeaderAccessor.STOMP_DESTINATION_HEADER);

        ServerMessage serverMessage = new ServerMessage("server", "승재님이 입장하였습니다.");

        template.convertAndSend(destination, serverMessage);
    }
}
