package com.example.training.controller;

import com.example.training.dto.ErrorResponse;
import com.example.training.dto.LoanApplicationResponse;
import com.example.training.model.User;
import com.example.training.security.AuthContext;
import com.example.training.security.AuthUtil;
import com.example.training.security.RoleValidator;
import com.example.training.service.LoanApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    // Helper: sama seperti di CustomerController
    private User resolveUser(String authHeader) {
        String token = AuthUtil.extractToken(authHeader);
        return AuthContext.getUserByToken(token);
    }

    // POST /api/v1/loan-applications
    // Hanya ADMIN dan STAFF. APPROVER → 403.
    // @RequestBody Map<String, Object> → Spring otomatis parse JSON jadi Map
    @PostMapping
    public ResponseEntity<?> createLoanApplication(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, Object> body) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        // body langsung diterusin ke service, service yang urus parsing field-nya
        LoanApplicationResponse response = loanApplicationService.createLoanApplication(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/loan-applications
    // Semua role boleh, tapi wajib login
    @GetMapping
    public ResponseEntity<?> getAllLoanApplications(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        List<LoanApplicationResponse> responses = loanApplicationService.getAllLoanApplications();
        return ResponseEntity.ok(responses);
    }

    // GET /api/v1/loan-applications/{id}
    // Semua role boleh, tapi wajib login
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

        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.error("LOAN_APPLICATION_NOT_FOUND", "Loan application not found", null));
        }

        return ResponseEntity.ok(response);
    }

    // PATCH /api/v1/loan-applications/{id}/approve
    // Hanya ADMIN dan APPROVER. STAFF → 403.
    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveLoanApplication(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "APPROVER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        LoanApplicationResponse response = loanApplicationService.approveLoanApplication(id);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.error("LOAN_APPLICATION_NOT_FOUND", "Loan application not found", null));
        }

        return ResponseEntity.ok(response);
    }

    // PATCH /api/v1/loan-applications/{id}/reject
    // Hanya ADMIN dan APPROVER. STAFF → 403.
    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectLoanApplication(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "APPROVER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        LoanApplicationResponse response = loanApplicationService.rejectLoanApplication(id);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.error("LOAN_APPLICATION_NOT_FOUND", "Loan application not found", null));
        }

        return ResponseEntity.ok(response);
    }
}