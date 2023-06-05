package com.hong.chatservice.member.application.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    static final int EXPIRATION_TIME = 60000 * 10;
    static final String SECRET_KEY = "secret";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    
    public String createJwt(Long memberId, String memberName){

        JwtBuilder jwtToken = Jwts.builder()
                .setSubject(memberName)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("id", memberId)
                .claim("username", memberName)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY);

        return TOKEN_PREFIX+jwtToken;
    }

    public String validateToken(String authorizationHeader) throws IllegalAccessException {
        validationAuthorizationHeader(authorizationHeader);
        String token = extractToken(authorizationHeader);

        String username = null;
        try {
            username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("username").toString();
        } catch (JwtException e) {
            throw new IllegalAccessException();
        }

        return username;

    }

    private void validationAuthorizationHeader(String header) throws IllegalAccessException {
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new IllegalAccessException();
        }
    }

    public String extractToken(String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX,"");
    }
}
