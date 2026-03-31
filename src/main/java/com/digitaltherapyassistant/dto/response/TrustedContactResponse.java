package com.digitaltherapyassistant.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TrustedContactResponse {
    private String id ;
    private String name ;
    private String phone ;
    private String relationship ;
}
