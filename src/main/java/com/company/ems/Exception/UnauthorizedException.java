package com.company.ems.Exception;

public class UnauthorizedException extends EMSException {
    public UnauthorizedException(String message) {
        super(message, 401);
    }
}
