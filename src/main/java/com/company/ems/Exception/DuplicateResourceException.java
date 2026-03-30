package com.company.ems.Exception;
public class DuplicateResourceException extends EMSException {
    public DuplicateResourceException(String message) {
        super(message, 409);
    }
}