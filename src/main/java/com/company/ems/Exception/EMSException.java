package com.company.ems.Exception;
public class EMSException extends RuntimeException {
    private int statusCode;
    public EMSException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
