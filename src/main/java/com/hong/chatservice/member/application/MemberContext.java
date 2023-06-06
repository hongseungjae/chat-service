package com.hong.chatservice.member.application;

import com.hong.chatservice.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

public class MemberContext extends User {

    private final Member member;

    public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getMemberName(), member.getPassword(), authorities);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        member.getRoleList().forEach(r -> {
            authorities.add(()->{ return r;});
        });
        return authorities;
    }
}