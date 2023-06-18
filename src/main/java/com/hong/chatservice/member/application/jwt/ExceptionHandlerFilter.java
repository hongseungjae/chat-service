package com.hong.chatservice.member.application.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.chatservice.setting.exception.BusinessException;
import com.hong.chatservice.setting.exception.CustomErrorResponse;
import com.hong.chatservice.setting.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            setErrorResponse(response);
        } catch (JwtException | IllegalArgumentException e) {
        }
    }

    private void setErrorResponse(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(ErrorCode.INVALID_USER);

        try {
            String json = new ObjectMapper().writeValueAsString(customErrorResponse);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}