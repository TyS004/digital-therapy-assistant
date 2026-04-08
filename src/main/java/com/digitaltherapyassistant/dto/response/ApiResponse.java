package com.digitaltherapyassistant.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// taken from commission calculator example

public class ApiResponse<T> {
    @Getter
    @Setter
    private boolean success ;

    @Getter
    @Setter
    private String message ;

    @Getter
    @Setter
    private T data ;

    @Getter
    @Setter
    private LocalDateTime timestamp ;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now() ;
    }

    public ApiResponse(boolean success, String message, T data) {
        this() ;
        this.success = success ;
        this.message = message ;
        this.data = data ;
    }

    // Factory method for successful api calls
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data) ;
    }

    // Factory method for error api calls
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data) ;
    }
}
