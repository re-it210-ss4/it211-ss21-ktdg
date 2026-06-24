package com.example.base_spring_boot.models.dtos.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenRefreshResponse {

    private String accessToken;

    private String refreshToken;
}