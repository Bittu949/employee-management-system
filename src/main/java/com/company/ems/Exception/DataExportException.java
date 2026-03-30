package com.company.ems.Exception;

public class DataExportException extends EMSException {
    public DataExportException(String message) {
        super(message, 500);
    }
}
