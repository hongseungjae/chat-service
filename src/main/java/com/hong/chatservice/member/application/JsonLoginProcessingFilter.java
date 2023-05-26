package com.hong.chatservice.member.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final String CONTENT_TYPE = "application/json";//json 타입의 데이터로만 로그인을 진행한다.
    private final ObjectMapper objectMapper;

    public JsonLoginProcessingFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        UserDto userDto = objectMapper.readValue(request.getReader(), UserDto.class);
        if (StringUtils.isEmpty(userDto.getUsername()) || StringUtils.isEmpty(userDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Data
    private static class UserDto {
        String username;
        String password;
    }
}