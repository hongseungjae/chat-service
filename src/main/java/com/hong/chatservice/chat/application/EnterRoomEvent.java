package com.hong.chatservice.chat.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
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

        log.info("message = {}, {}", sha.getMessage(), sha.getSessionId());
        System.out.println("sha.getSubscriptionId() = " + sha.getSubscriptionId());
        StompCommand command = sha.getCommand();
        System.out.println("command = " + command.name());
        System.out.println("command.toString() = " + command.toString());
        //log.info("enter member = {}", event.getUser().getName());
        //template.convertAndSendToUser(event.getUser().getName(), "/queue/notify", "GREETINGS");
        //template.convertAndSend("/topic/chat/" + chatMessage.getRoomId(), chatMessage);
    }
}
