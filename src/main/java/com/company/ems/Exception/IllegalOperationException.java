package com.company.ems.Exception;
public class IllegalOperationException extends EMSException {
    public IllegalOperationException(String message) {
        super(message, 400);
    }
}