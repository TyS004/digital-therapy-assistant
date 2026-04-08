package com.digitaltherapyassistant.exception;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {

    @Getter
    private final String resource ;

    @Getter
    private final String field ;

    @Getter
    private final String fieldInput ;

    public ResourceNotFoundException(String resource, String field, String fieldInput) {
        super("" + resource + " with field" + field + ": " + fieldInput + " was not found") ;
        this.resource = resource ;
        this.field = field ;
        this.fieldInput = fieldInput ;
    }
}
