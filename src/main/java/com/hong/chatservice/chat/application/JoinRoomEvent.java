package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.MessageType;
import com.hong.chatservice.chat.presentation.ServerMessage;
import com.hong.chatservice.member.application.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class JoinRoomEvent {

    private final SimpMessageSendingOperations template;
    private final JwtUtil jwtUtil;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {

        Set<SimpUser> users = simpUserRegistry.getUsers();
        System.out.println("users.size() = " + users.size());
        for (SimpUser user : users) {
            System.out.println("user = " + user);
        }

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String token = accessor.getFirstNativeHeader(JwtUtil.HEADER_STRING);
        String username = jwtUtil.validateToken(token);

        String destination = accessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_DESTINATION_HEADER);
        ServerMessage serverMessage = new ServerMessage(EventProperties.SERVER_NAME, username, String.format("%s님이 입장하였습니다.", username), MessageType.JOIN);
        log.info("server 입장 : {}",serverMessage);
        accessor.getSessionAttributes().put(EventProperties.SESSION_USERNAME, username);
        accessor.getSessionAttributes().put(EventProperties.SESSION_DESTINATION, destination);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        template.convertAndSend(destination, serverMessage);
    }
}
