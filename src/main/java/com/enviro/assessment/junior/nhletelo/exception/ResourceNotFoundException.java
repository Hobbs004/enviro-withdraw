package com.enviro.assessment.junior.nhletelo.exception;

/**
 * Thrown when a requested entity (investor, product, notice) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
