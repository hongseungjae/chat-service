package com.hong.chatservice.member.application;

import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByMemberName(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("UsernameNotFoundException");
                });

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRoleList().forEach(role ->
                authorities.add(()-> role));

        MemberContext memberContext = new MemberContext(member, authorities);

        return memberContext;
    }
}
