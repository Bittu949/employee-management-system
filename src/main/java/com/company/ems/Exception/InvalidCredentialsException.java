package com.company.ems.Exception;

public class InvalidCredentialsException extends EMSException {
    public InvalidCredentialsException(String message) {
        super(message, 401);
    }
}
