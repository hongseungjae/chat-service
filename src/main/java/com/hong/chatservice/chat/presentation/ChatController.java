package com.hong.chatservice.chat.presentation;

import com.hong.chatservice.chat.application.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;



    @MessageMapping("/chat")
    public void createPost(@Headers Map<String, Object> headers,
                           @RequestBody @Valid ChatMessage chatMessage) {

        template.convertAndSend("/topic/chat/" + chatMessage.getRoomId(), chatMessage);
        //chatService.createChat(chatMessage);
        log.info("chatMessage = {}", chatMessage);
    }
}