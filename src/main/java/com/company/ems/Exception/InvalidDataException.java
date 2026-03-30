package com.company.ems.Exception;

public class InvalidDataException extends EMSException {
    public InvalidDataException(String message) {
        super(message, 400);
    }
}
