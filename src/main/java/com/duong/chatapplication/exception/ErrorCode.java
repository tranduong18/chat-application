package com.duong.chatapplication.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "Unknow Exception", HttpStatus.INTERNAL_SERVER_ERROR),

    FORBIDDEN(403, "Access denied. You do not have the required permissions to access this resource.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(401, "Authentication is required. Please provide a valid token to access this resource.", HttpStatus.UNAUTHORIZED),

    TOKEN_GENERATION_FAILED(500, "Failed to generate JWT token", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_EXISTED(400, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),

    ;
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
