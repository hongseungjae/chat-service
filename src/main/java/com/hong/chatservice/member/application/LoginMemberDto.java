package com.hong.chatservice.member.application;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginMemberDto {
    Long id;
    String memberName;
}
