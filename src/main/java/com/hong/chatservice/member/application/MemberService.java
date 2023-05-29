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
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String encodedPassword = passwordEncoder.encode("1");
        Member hong = new Member("hong",encodedPassword);
        Member jae = new Member("jae",encodedPassword);
        Member kai = new Member("kai",encodedPassword);
        Member proto = new Member("proto",passwordEncoder.encode("1111"));

        memberRepository.save(hong);
        memberRepository.save(jae);
        memberRepository.save(kai);
        memberRepository.save(proto);

    }


}