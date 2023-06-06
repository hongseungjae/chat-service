package com.hong.chatservice.member.application;

import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByMemberName(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("UsernameNotFoundException");
                });
        System.out.println("MemberDetailsService.loadUserByUsername");

        //List<GrantedAuthority> roles = new ArrayList<>();
        //roles.add(new SimpleGrantedAuthority(("role_user")));
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        member.getRoleList().forEach(r -> {
            authorities.add(()->{ return r;});
        });
        MemberContext memberContext = new MemberContext(member, authorities);

        return memberContext;
    }
}
