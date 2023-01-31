package com.example.authenticationservice.dto;

import lombok.Data;

@Data
public class JwtRefreshRequest {
    private String refreshToken;
}
