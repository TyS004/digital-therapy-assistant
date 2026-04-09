package com.digitaltherapyassistant.dto.response.crisis;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CopingStrategyResponse {
    @NotBlank(message = "id is required")
    private UUID id ;

    @NotBlank(message = "name is required")
    private String name ;

    @NotBlank(message = "description is required")
    private String description ;

    @NotBlank(message = "steps are required")
    private List<String> steps ;
}
