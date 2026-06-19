package com.example.training.exception;

public class LoanApplicationNotFoundException extends RuntimeException {
    public String code;
    public String message;
    public String errors;

    public LoanApplicationNotFoundException(String code, String message, String errors) {
        super(message);
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
}