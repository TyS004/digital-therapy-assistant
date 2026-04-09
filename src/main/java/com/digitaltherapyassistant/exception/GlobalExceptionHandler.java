package com.digitaltherapyassistant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    //BAD REQUEST
    @ExceptionHandler(DigitalTherapyException.class)
    public ResponseEntity<?> HandleBadRequest(DigitalTherapyException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getReason());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> HandleInValidConstriants(ConstraintViolationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<?> HandleUnexpectedType(UnexpectedTypeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    //SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> HttpException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
