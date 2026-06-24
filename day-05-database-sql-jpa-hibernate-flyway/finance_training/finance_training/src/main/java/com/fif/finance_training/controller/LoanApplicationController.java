package com.fif.finance_training.controller;

import com.fif.finance_training.dto.*;
import com.fif.finance_training.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.jboss.logging.MDC;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    @PostMapping("/loan-applications")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> createLoan(
            @Valid @RequestBody CreateLoanApplicationRequest request) {
        
        LoanApplicationResponse response = loanApplicationService.createLoan(request);
        
        ApiResponse<LoanApplicationResponse> apiResponse = ApiResponse.<LoanApplicationResponse>builder()
                .success(true)
                .message("Loan application submitted successfully")
                .data(response)
                .build();
                
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/loan-applications/{id}")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> getLoanById(
            @PathVariable("id") Long id) {
        
        LoanApplicationResponse response = loanApplicationService.getLoanById(id);
        
        ApiResponse<LoanApplicationResponse> apiResponse = ApiResponse.<LoanApplicationResponse>builder()
                .success(true)
                .message("Loan application retrieved successfully")
                .data(response)
                .build();
                
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/loan-applications")
    public ResponseEntity<ApiResponse<PagedResponse<LoanApplicationResponse>>> getAllLoans(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        PagedResponse<LoanApplicationResponse> response;
        
        if (status != null && !status.trim().isEmpty()) {
            response = loanApplicationService.getLoansByStatus(status.toUpperCase(), pageable);
        } else if (startDate != null && endDate != null) {
            response = loanApplicationService.getLoansByDateRange(startDate, endDate, pageable);
        } else {
            response = loanApplicationService.getAllLoans(pageable);
        }
        
        ApiResponse<PagedResponse<LoanApplicationResponse>> apiResponse = ApiResponse.<PagedResponse<LoanApplicationResponse>>builder()
                .success(true)
                .message("Loan applications retrieved successfully")
                .data(response)
                .correlationId(org.slf4j.MDC.get("correlationId"))
                .build();
                
                
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/customers/{customer_id}/loan-applications")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoansByCustomerId(
            @PathVariable("customer_id") Long customerId) {
        
        List<LoanApplicationResponse> response = loanApplicationService.getLoansByCustomerId(customerId);
        
        ApiResponse<List<LoanApplicationResponse>> apiResponse = ApiResponse.<List<LoanApplicationResponse>>builder()
                .success(true)
                .message("Customer's loan applications retrieved successfully")
                .data(response)
                .build();
                
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/loan-applications/{id}/status")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> updateLoanStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateLoanStatusRequest request) {
        
        LoanApplicationResponse response = loanApplicationService.updateLoanStatus(id, request);
        
        ApiResponse<LoanApplicationResponse> apiResponse = ApiResponse.<LoanApplicationResponse>builder()
                .success(true)
                .message("Loan application status updated successfully")
                .data(response)
                .build();
                
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/loan-applications/summary/status")
    public ResponseEntity<ApiResponse<List<LoanStatusSummaryResponse>>> getLoanStatusSummary() {
        List<LoanStatusSummaryResponse> response = loanApplicationService.getLoanStatusSummary();
        ApiResponse<List<LoanStatusSummaryResponse>> apiResponse = ApiResponse.<List<LoanStatusSummaryResponse>>builder()
                .success(true)
                .message("Loan status summary retrieved successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/customers/outstanding")
    public ResponseEntity<ApiResponse<List<CustomerOutstandingResponse>>> getCustomerOutstanding() {
        List<CustomerOutstandingResponse> response = loanApplicationService.getCustomerOutstanding();
        ApiResponse<List<CustomerOutstandingResponse>> apiResponse = ApiResponse.<List<CustomerOutstandingResponse>>builder()
                .success(true)
                .message("Customer outstanding amounts retrieved successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}