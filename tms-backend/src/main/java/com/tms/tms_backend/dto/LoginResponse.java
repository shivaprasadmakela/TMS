package com.tms.tms_backend.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final UserDTO user;
    private final String token;
    private long expirationTime;

    public LoginResponse(UserDTO user, String token, long expirationTime) {
        this.user = user;
        this.token = token;
        this.expirationTime = expirationTime;
    }

}
