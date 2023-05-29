package com.hong.chatservice.setting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_USER(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력했습니다. "),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request."),

    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "Unauthorized."),

    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "Forbidden."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Not allowed method."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error.");

    private final HttpStatus httpStatus;
    private final String message;
}