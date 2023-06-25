package com.hong.chatservice.chat.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatStatusController {

    private final SimpUserRegistry simpUserRegistry;

    @GetMapping("/users")
    public ResponseEntity users(){

        ArrayList<String> userList = new ArrayList<>();
        Set<SimpUser> users = simpUserRegistry.getUsers();
        for (SimpUser user : users) {
            userList.add(user.toString());
        }

        return ResponseEntity.ok(userList);
    }
}