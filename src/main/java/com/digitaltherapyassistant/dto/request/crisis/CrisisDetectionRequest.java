package com.digitaltherapyassistant.dto.request.crisis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrisisDetectionRequest {

    @NotBlank(message = "Input text is required")
    private String body ;
}
