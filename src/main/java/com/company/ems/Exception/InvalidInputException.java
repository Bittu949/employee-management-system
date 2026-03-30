package com.company.ems.Exception;

public class InvalidInputException extends EMSException {
    public InvalidInputException(String message) {
        super(message, 400);
    }
}