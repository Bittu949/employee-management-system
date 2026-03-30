package com.company.ems.Exception;

public class InvalidPasswordException extends EMSException {
    public InvalidPasswordException(String message) {
        super(message, 400);
    }
}
