package com.hong.chatservice.member.application.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.chatservice.member.application.MemberContext;
import com.hong.chatservice.setting.exception.BusinessException;
import com.hong.chatservice.setting.exception.ErrorCode;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String CONTENT_TYPE = "application/json";

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        UserDto userDto = null;

        try {
            userDto = objectMapper.readValue(request.getReader(), UserDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (StringUtils.isEmpty(userDto.getUsername()) || StringUtils.isEmpty(userDto.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_USER);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        MemberContext principalDetails = (MemberContext) authResult.getPrincipal();

        String jwt = jwtUtil.createJwt(principalDetails.getMember().getId(), principalDetails.getMember().getMemberName());

        response.addHeader(JwtUtil.HEADER_STRING, jwt);
    }

    @Data
    private static class UserDto {
        String username;
        String password;
    }
}
