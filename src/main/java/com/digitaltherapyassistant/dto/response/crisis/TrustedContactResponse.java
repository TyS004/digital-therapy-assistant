package com.digitaltherapyassistant.dto.response.crisis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class TrustedContactResponse {

    @NotBlank(message = "trustedContactId is required")
    private UUID id ;

    @NotBlank(message = "name required")
    private String name ;

    @NotBlank(message = "phone number required")
    private String phone ;

    @NotBlank(message = "relationship required")
    private String relationship ;
}
