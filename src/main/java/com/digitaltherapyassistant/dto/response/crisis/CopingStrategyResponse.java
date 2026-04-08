package com.digitaltherapyassistant.dto.response.crisis;

import lombok.Data;

import java.util.List;

@Data
public class CopingStrategyResponse {
    private String id ;
    private String name ;
    private String description ;
    private List<String> steps ;
}
