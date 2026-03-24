package com.digitaltherapyassistant.dto.response;

import lombok.Getter;
import lombok.Setter;

public class TrustedContactResponse {
    @Getter
    @Setter
    private String id ;

    @Getter
    @Setter
    private String name ;

    @Getter
    @Setter
    private String phone ;

    @Getter
    @Setter
    private String relationship ;
}
