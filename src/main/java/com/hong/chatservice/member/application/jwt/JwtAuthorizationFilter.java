package com.hong.chatservice.member.application.jwt;

import com.hong.chatservice.member.application.MemberContext;
import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            e.printStackTrace();
            chain.doFilter(request, response);
            return;
        }

        if (username != null) {
            Member member = memberRepository.findByMemberName(username)
                    .orElseThrow(() -> {
                        throw new RuntimeException();
                    });


                Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                member.getRoleList().forEach(r -> {
                    authorities.add(()->{ return r;});
                });


            MemberContext principalDetails = new MemberContext(member, authorities);

            Authentication authenticate = new UsernamePasswordAuthenticationToken(principalDetails,principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }

        chain.doFilter(request, response);
    }
}
