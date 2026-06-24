package com.example.base_spring_boot.models.dtos.req;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}