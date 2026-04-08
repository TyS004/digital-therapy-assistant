package com.digitaltherapyassistant.dto.response.crisis;

import lombok.Data;

import java.util.UUID;

@Data
public class TrustedContactResponse {
    private UUID id ;
    private String name ;
    private String phone ;
    private String relationship ;
}
