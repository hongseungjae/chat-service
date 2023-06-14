package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.ServerMessage;
import com.hong.chatservice.member.application.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnterRoomEvent {

    private final SimpMessageSendingOperations template;
    private final JwtUtil jwtUtil;
    static private final String SERVER_NAME = "server";


    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String token = accessor.getFirstNativeHeader(JwtUtil.HEADER_STRING);
        String username = jwtUtil.validateToken(token);

        String destination = accessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_DESTINATION_HEADER);
        ServerMessage serverMessage = new ServerMessage(SERVER_NAME, String.format("%s님이 입장하였습니다.", username));
        log.info("server 입장 : {}",serverMessage);
        accessor.getSessionAttributes().put("username", username);

        template.convertAndSend(destination, serverMessage);
    }
}
