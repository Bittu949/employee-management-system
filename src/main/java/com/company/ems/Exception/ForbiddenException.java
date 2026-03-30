package com.company.ems.Exception;
public class ForbiddenException extends EMSException {
    public ForbiddenException(String message) {
        super(message, 403);
    }
}
