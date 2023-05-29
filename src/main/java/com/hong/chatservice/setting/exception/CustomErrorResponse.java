package com.hong.chatservice.setting.exception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomErrorResponse {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String error;
    private final String message;

    public CustomErrorResponse(ErrorCode errorCode) {
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
    }
}