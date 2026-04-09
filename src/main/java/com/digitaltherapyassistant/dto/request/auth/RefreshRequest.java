package com.digitaltherapyassistant.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {
    @NotBlank(message = "Refresh Token required")
    private String refreshToken;
}
