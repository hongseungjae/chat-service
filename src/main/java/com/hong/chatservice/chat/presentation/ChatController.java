package com.hong.chatservice.chat.presentation;

import com.hong.chatservice.chat.application.ChatService;
import com.hong.chatservice.member.application.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final JwtUtil jwtUtil;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void createPost(StompHeaderAccessor accessor,
                           @RequestBody @Valid ChatMessage chatMessage) {

        String token = accessor.getFirstNativeHeader(JwtUtil.HEADER_STRING);
        String username = jwtUtil.validateToken(token);
        chatMessage.setMemberName(username);

        log.info("chatMessage = {}", chatMessage);
        template.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
        //chatService.createChat(chatMessage);

    }
}