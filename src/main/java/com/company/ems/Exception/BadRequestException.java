package com.company.ems.Exception;

public class BadRequestException extends EMSException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}