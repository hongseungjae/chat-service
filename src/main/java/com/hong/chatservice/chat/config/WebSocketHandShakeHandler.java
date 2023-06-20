package com.hong.chatservice.chat.config;

import com.hong.chatservice.member.application.MemberContext;
import com.sun.security.auth.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class WebSocketHandShakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpHeaders headers = request.getHeaders();
        System.out.println("headers.size() = " + headers.size());
        for (String s : headers.keySet()) {
            System.out.println(s + " " + headers.get(s));
        }
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpHeaders headers1 = request.getHeaders();
        System.out.println("headers1 = " + headers1);


        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        String username = httpServletRequest.getParameter("name");
        UserPrincipal userPrincipal = new UserPrincipal(username);

        return userPrincipal;
    }
}
