package com.example.training.controller;

import com.example.training.dto.CreateLoanApplicationRequest;
import com.example.training.dto.ErrorResponse;
import com.example.training.dto.LoanApplicationResponse;
import com.example.training.exception.LoanApplicationNotFoundException;
import com.example.training.model.User;
import com.example.training.security.AuthContext;
import com.example.training.security.AuthUtil;
import com.example.training.security.RoleValidator;
import com.example.training.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    private User resolveUser(String authHeader) {
        String token = AuthUtil.extractToken(authHeader);
        return AuthContext.getUserByToken(token);
    }

    @PostMapping
    public ResponseEntity<?> createLoanApplication(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CreateLoanApplicationRequest request) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        LoanApplicationResponse response = loanApplicationService.createLoanApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllLoanApplications(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) String status,
            @RequestParam(name = "customer_id", required = false) Long customerId) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        List<LoanApplicationResponse> responses = loanApplicationService.getAllLoanApplications(status, customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanApplicationById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        LoanApplicationResponse response = loanApplicationService.getLoanApplicationById(id);

        // Kalau null, throw exception → ditangkap GlobalExceptionHandler → return 404
        if (response == null) {
            throw new LoanApplicationNotFoundException("LOAN_APPLICATION_NOT_FOUND", "Loan application not found", null);
        }

        return ResponseEntity.ok(response);
    }
    //cuma admin dan approver yg bs approve all nominal
    //kecuali 999jt up hanya manager
    //staff ->403
    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveLoanApplication(
            @RequestHeader(value = "Authorization", 
            required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }
        //ini ifelse kalo misal RoleValidator nya yang
        //notabene hanya bisa admin, approver, manager,
        //malah bukan role antara 3 itu,
        //dia jadi return error FORBIDDEN
        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "APPROVER", "MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }
        if ("MANAGER".equals(user.getRole())) {
            String result = loanApplicationService.approveByManager(id);
        
            if ("NOT_FOUND".equals(result)) {
                throw new LoanApplicationNotFoundException("LOAN_APPLICATION_NOT_FOUND", "Loan application not found", null);
            }

            if("BELOW_MINIMUM".equals(result)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "Manager can only approve loan above 999jt", null));
            }
            return ResponseEntity.ok(loanApplicationService.getLoanApplicationById(id));
        }

        LoanApplicationResponse response = loanApplicationService.approveLoanApplication(id);

        if (response == null) {
            throw new LoanApplicationNotFoundException("LOAN_APPLICATION_NOT_FOUND", "Loan application not found", null);
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectLoanApplication(
            @RequestHeader(value = "Authorization", 
            required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "APPROVER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", 
                    "You do not have permission to access this resource", 
                    null));
        }

        LoanApplicationResponse response = loanApplicationService.rejectLoanApplication(id);

        if (response == null) {
            throw new LoanApplicationNotFoundException("LOAN_APPLICATION_NOT_FOUND", 
            "Loan application not found", null);
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelLoanApplication(
            @RequestHeader(value = "Authorization", 
            required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", 
                    "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "APPROVER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", 
                    "You do not have permission to access this resource", null));
        }

        String result = loanApplicationService.cancelLoanApplication(id);

        if ("NOT_FOUND".equals(result)) {
            throw new LoanApplicationNotFoundException("LOAN_APPLICATION_NOT_FOUND", "Loan application not found", null);
        }

        if ("INVALID_STATUS".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.error("INVALID_STATUS", 
                    "Only SUBMITTED loan can be cancelled", 
                    null));
        }

        return ResponseEntity.ok(loanApplicationService.getCancelledLoan(id));
    }
}