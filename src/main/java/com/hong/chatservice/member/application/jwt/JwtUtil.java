package com.hong.chatservice.member.application.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    static final public int EXPIRATION_TIME = 60000 * 1000; // 60초 * 10 = 10분
    static final public String SECRET_KEY = "secret";
    static final public String TOKEN_PREFIX = "Bearer ";
    static final public String HEADER_STRING = "Authorization";
    
    public String createJwt(Long memberId, String memberName){
        String jwtToken = Jwts.builder()
                .setSubject(memberName)
                .claim("id", memberId)
                .claim("username", memberName)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return TOKEN_PREFIX+jwtToken;
    }

    public String validateToken(String authorizationHeader) {
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
            e.printStackTrace();
            throw new RuntimeException("Jwt 검증에 실패했습니다.");
        }

        if (username == null) {
            throw new RuntimeException("Jwt 토큰에서 username을 얻어오는데 실패했습니다.");
        }

        return username;

    }

    private void validationAuthorizationHeader(String header) {
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new RuntimeException("헤더값이 Bearer 타입이 아니거나 null 입니다.");
        }
    }

    public String extractToken(String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX,"");
    }
}
