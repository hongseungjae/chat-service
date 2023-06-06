package com.hong.chatservice.member.application;

import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberInit {
    private final MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String encodedPassword = passwordEncoder.encode("1");
        Member hong = new Member("hong",encodedPassword,"ROLE_USER");
        Member jae = new Member("jae",encodedPassword,"ROLE_USER");
        Member kai = new Member("kai",encodedPassword,"ROLE_USER");
        Member proto = new Member("proto",passwordEncoder.encode("1111"),"ROLE_USER");

        memberRepository.save(hong);
        memberRepository.save(jae);
        memberRepository.save(kai);
        memberRepository.save(proto);

    }


}