package com.hong.chatservice.chat.application;

import com.hong.chatservice.chat.presentation.MessageType;
import com.hong.chatservice.chat.presentation.ServerMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaveRoomEvent {

    private final SimpMessagingTemplate template;

    @Autowired
    private SimpUserRegistry simpUserRegistry;
    @EventListener
    //@Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public void SessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        System.out.println("leave event user = " + user);

        int userCount = simpUserRegistry.getUserCount();
        System.out.println("leave userCount = " + userCount);

        String username = (String) accessor.getSessionAttributes().get(EventProperties.SESSION_USERNAME);
        String destination = (String) accessor.getSessionAttributes().get(EventProperties.SESSION_DESTINATION);


        Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription ->
                subscription.getDestination().equals(destination));

        for (SimpSubscription subscription : subscriptions) {
            String name = subscription.getSession().getUser().getName();
            System.out.println("leave name = " + name);
        }

        ServerMessage serverMessage = new LeaveServerMessage(EventProperties.SERVER_NAME, String.format("%s님이 나갔습니다.", username), MessageType.LEAVE, username);
        log.info("server 퇴장 : {}",serverMessage);

        template.convertAndSend(destination, serverMessage);
    }

}
