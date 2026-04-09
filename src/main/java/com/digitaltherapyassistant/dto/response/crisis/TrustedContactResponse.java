package com.digitaltherapyassistant.dto.response.crisis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TrustedContactResponse {

    @NotNull(message = "trustedContactId is required")
    private UUID id ;

    @NotBlank(message = "name required")
    private String name ;

    @NotBlank(message = "phone number required")
    private String phone ;

    @NotBlank(message = "relationship required")
    private String relationship ;
}
