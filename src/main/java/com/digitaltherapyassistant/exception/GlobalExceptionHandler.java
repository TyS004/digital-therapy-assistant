package com.digitaltherapyassistant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    //BAD REQUEST
    @ExceptionHandler(DigitalTherapyException.class)
    public ResponseEntity<?> HandleBadRequest(DigitalTherapyException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getReason());
    }

    //SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> HttpException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
