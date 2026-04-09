package com.digitaltherapyassistant.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ApiErrorResponse {

    @Getter
    @Setter
    private int status ;

    @Getter
    @Setter
    private String code ;

    @Getter
    @Setter
    private String message ;

    @Getter
    @Setter
    private LocalDateTime timestamp ;

    @Getter
    @Setter
    private Map<String, List<String>> validationErrors ;

    public ApiErrorResponse() {
        this.timestamp = LocalDateTime.now() ;
    }

    public ApiErrorResponse(int status, String code, String message) {
        this() ;
        this.status = status ;
        this.code = code ;
        this.message = message ;
    }
}
