package com.hong.chatservice.member.application.jwt;

import com.hong.chatservice.member.application.MemberContext;
import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(JwtUtil.HEADER_STRING);

        String username = null;
        try {
            username = jwtUtil.validateToken(header);
        } catch (IllegalAccessException e) {
            chain.doFilter(request, response);
            return;
        }

        if (username != null) {
            Member member = memberRepository.findByMemberName(username)
                    .orElseThrow(() -> {
                        throw new RuntimeException();
                    });

            MemberContext principalDetails = new MemberContext(member);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
