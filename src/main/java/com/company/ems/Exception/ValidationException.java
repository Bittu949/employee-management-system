package com.company.ems.Exception;

public class ValidationException extends EMSException {
    public ValidationException(String message) {
        super(message, 400);
    }
}
