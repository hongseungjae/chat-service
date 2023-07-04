package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.MessageType;
import com.hong.chatservice.chat.presentation.ServerMessage;
import com.hong.chatservice.member.application.jwt.JwtUtil;
import com.hong.chatservice.room.application.RoomResponseDto;
import com.hong.chatservice.room.application.RoomService;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class JoinRoomEvent {

    private final SimpMessageSendingOperations template;
    private final JwtUtil jwtUtil;
    private final RoomService roomService;

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String token = accessor.getFirstNativeHeader(JwtUtil.HEADER_STRING);
        String username = jwtUtil.validateToken(token);
        String destination = accessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_DESTINATION_HEADER);

        Long roomId = Long.valueOf(destination.split("/")[1]);
        roomService.joinRoom(roomId, username);

        ArrayList<String> memberNames = new ArrayList();
        RoomResponseDto roomResponseDto = roomService.retrieveRoom(roomId);
        roomResponseDto.getParticipantsInfo().stream()
                .forEach(p -> memberNames.add(p.getMemberName()));

        ServerMessage serverMessage = JoinServerMessage.builder()
                .sourceName(EventProperties.SERVER_NAME)
                .content(String.format("%s님이 입장하였습니다.", username))
                .messageType(MessageType.JOIN)
                .memberNames(memberNames)
                .build();

        log.info("server 입장 : {}", serverMessage);
        accessor.getSessionAttributes().put(EventProperties.SESSION_USERNAME, username);
        accessor.getSessionAttributes().put(EventProperties.SESSION_DESTINATION, destination);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        template.convertAndSend(destination, serverMessage);
    }
}
