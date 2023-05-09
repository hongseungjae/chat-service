package com.hong.chatservice.member.application;

import com.hong.chatservice.member.domain.Member;
import com.hong.chatservice.member.infrastructure.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member hong = new Member("hong");
        Member jae = new Member("jae");
        Member kai = new Member("kai");

        memberRepository.save(hong);
        memberRepository.save(jae);
        memberRepository.save(kai);
    }


}