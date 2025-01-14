package com.example.bankTransfers.exception;

public class TransferNotFoundException extends RuntimeException {

    public TransferNotFoundException(String message) {
        super(message);
    }
    
    public TransferNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
