package com.hong.chatservice.setting.JwtMethodArgument;

import lombok.Getter;

@Getter
public class MemberDto {

    Long memberId;
    String memberName;

    public MemberDto(Long memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
