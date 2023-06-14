package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.ServerMessage;
import com.hong.chatservice.member.application.MemberContext;
import com.hong.chatservice.member.application.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaveRoomEvent {

    private final SimpMessagingTemplate template;
    private final JwtUtil jwtUtil;
    static private final String SERVER_NAME = "server";

    @EventListener
    public void SessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());


        //String token = accessor.getFirstNativeHeader(JwtUtil.HEADER_STRING);
        //String username = jwtUtil.validateToken(token);

        MessageHeaders messageHeaders = accessor.toMessageHeaders();
        for (String s : messageHeaders.keySet()) {
            System.out.println(s);
            Object o = messageHeaders.get(s);
            System.out.println("o = " + o);
        }

        String subscription = accessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_SUBSCRIPTION_HEADER);
        System.out.println("subscription = " + subscription);

        //String destination = accessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_DESTINATION_HEADER);
        String username = (String) accessor.getSessionAttributes().get("username");

        ServerMessage serverMessage = new ServerMessage(SERVER_NAME, String.format("%s님이 나갔습니다.", username));
        log.info("server 퇴장 : {}",serverMessage);

        template.convertAndSend("/topic/public", serverMessage);
    }

}
