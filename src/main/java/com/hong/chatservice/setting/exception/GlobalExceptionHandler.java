package com.hong.chatservice.setting.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CustomErrorResponse> handleCustomException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        System.out.println("errorCode = " + errorCode);
        return handleExceptionInternal(errorCode);
    };

    private ResponseEntity<CustomErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new CustomErrorResponse(errorCode));
    }
}