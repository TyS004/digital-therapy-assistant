package com.digitaltherapyassistant.dto.response.crisis;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CopingStrategyResponse {
    private UUID id ;
    private String name ;
    private String description ;
    private List<String> steps ;
}
