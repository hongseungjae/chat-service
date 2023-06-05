package com.hong.chatservice.member.controller;

import com.hong.chatservice.member.application.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LoginTestController {
    @RequestMapping(path = "/login-test", method = RequestMethod.GET)
    public String checkLogin(Authentication authentication) {
        MemberContext memberContext = (MemberContext) authentication.getPrincipal();
        System.out.println("memberContext.getAccount().getMemberName() = " + memberContext.getMember().getMemberName());
        return "hello";
    }





}