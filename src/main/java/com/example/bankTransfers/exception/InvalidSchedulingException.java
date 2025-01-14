package com.example.bankTransfers.exception;

public class InvalidSchedulingException extends RuntimeException {

    public InvalidSchedulingException(String message) {
        super(message);
    }
    
    public InvalidSchedulingException(String message, Throwable cause) {
        super(message, cause);
    }
}