package com.hong.chatservice.member.application.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.chatservice.setting.exception.CustomErrorResponse;
import com.hong.chatservice.setting.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            CustomErrorResponse customErrorResponse = new CustomErrorResponse(ErrorCode.INVALID_USER);
            String json = new ObjectMapper().writeValueAsString(customErrorResponse);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}