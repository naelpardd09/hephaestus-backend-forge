package com.example.training.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import com.example.training.dto.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<FieldErrorResponse> details = new ArrayList<>();

        for (FieldError error : fieldErrors) {
            FieldErrorResponse response = FieldErrorResponse
                    .builder()
                    .message(error.getDefaultMessage())
                    .field(error.getField())
                    .build();
            details.add(response);
        }
        return ResponseEntity.badRequest().body(ErrorResponse.error("INVALID_REQUEST", "Invalid Request", details));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse<Void>> customerNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.error("CUSTOMER_NOT_FOUND", ex.getMessage(), null));
    }

    // Handler baru untuk LoanApplicationNotFoundException.
    // Polanya sama persis dengan customerNotFound di atas.
    @ExceptionHandler(LoanApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse<Void>> loanNotFound(LoanApplicationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.error("LOAN_APPLICATION_NOT_FOUND", ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.error("INTERNAL_SERVER_ERROR", "Unexpected error occurred", null));
    }
}