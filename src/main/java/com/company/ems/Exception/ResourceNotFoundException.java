package com.company.ems.Exception;

public class ResourceNotFoundException extends EMSException {
    public ResourceNotFoundException(String message) {
        super(message ,404);
    }
}