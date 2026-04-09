package com.digitaltherapyassistant.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email required")
    private String email;
    @NotBlank(message = "Email required")
    private String password;
    @NotBlank(message = "Email required")
    private String name;
}