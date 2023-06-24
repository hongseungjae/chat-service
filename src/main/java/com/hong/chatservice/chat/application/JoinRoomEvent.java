package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.MessageType;
import com.hong.chatservice.chat.presentation.ServerMessage;
import com.hong.chatservice.member.application.jwt.JwtUtil;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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

    @Autowired
    private SimpUserRegistry simpUserRegistry;


    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String token = accessor.getFirstNativeHeader(JwtUtil.HEADER_STRING);
        String username = jwtUtil.validateToken(token);
        String destination = accessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_DESTINATION_HEADER);

        UserPrincipal userPrincipal = new UserPrincipal(username);
        ((DefaultSimpUserRegistry) simpUserRegistry).onApplicationEvent(new SessionConnectedEvent(this, event.getMessage(), userPrincipal));
        ((DefaultSimpUserRegistry) simpUserRegistry).onApplicationEvent(new SessionSubscribeEvent(this, event.getMessage(), userPrincipal));


        Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription ->
                subscription.getDestination().equals(destination));

        ArrayList<String> userNames = new ArrayList();
        for (SimpSubscription subscription : subscriptions) {
            userNames.add(subscription.getSession().getUser().getName());
        }

        ServerMessage serverMessage = JoinServerMessage.builder()
                .sourceName(EventProperties.SERVER_NAME)
                .content(String.format("%s님이 입장하였습니다.", username))
                .messageType(MessageType.JOIN)
                .userNames(userNames)
                .build();

        log.info("server 입장 : {}", serverMessage);
        accessor.getSessionAttributes().put(EventProperties.SESSION_USERNAME, username);
        accessor.getSessionAttributes().put(EventProperties.SESSION_DESTINATION, destination);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        template.convertAndSend(destination, serverMessage);
    }
}
