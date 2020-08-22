package com.hubt.api.auth.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String accessToken;

    private Object user;

    public JwtAuthenticationResponse(String accessToken, Object user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
