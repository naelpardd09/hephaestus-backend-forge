package com.example.training.controller;
//-----
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.example.training.service.CustomerService;
import com.example.training.model.User;
import com.example.training.security.AuthContext;
import com.example.training.security.AuthUtil;
import com.example.training.security.RoleValidator;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.example.training.dto.*;
//________________________________________________________
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "Customer", description = "Customer Management API")
@RestController
@RequestMapping("/api/v1/customers") //
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Helper: baca token dari header → cari User-nya
    // Kalau token tidak ada atau tidak valid, return null
    private User resolveUser(String authHeader) {
        String token = AuthUtil.extractToken(authHeader);
        return AuthContext.getUserByToken(token);
    }

    // POST /api/v1/customers
    // Yang boleh: ADMIN, STAFF. APPROVER → 403.
    @Operation(summary = "Create Customer")
    @ApiResponse(responseCode = "201", description = "Berhasil dibuat")
    @ApiResponse(responseCode = "401", description = "Token tidak valid",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Tidak punya akses",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<?> createCustomer(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody @Valid CreateCustomerRequest request) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/customers
    // Semua role boleh, tapi wajib login
    @Operation(summary = "Get All Customers")
    @ApiResponse(responseCode = "200", description = "Berhasil")
    @ApiResponse(responseCode = "401", description = "Token tidak valid",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<?> getAllCustomer(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        return ResponseEntity.ok(customerService.getCustomers(email, page, size));
    }

    // GET /api/v1/customers/{id}
    // Semua role boleh, tapi wajib login
    @Operation(summary = "Get Customer by ID")
    @ApiResponse(responseCode = "200", description = "Berhasil ditemukan")
    @ApiResponse(responseCode = "401", description = "Token tidak valid",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Customer tidak ditemukan",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        // getCustomerById di service sudah throw CustomerNotFoundException
        // yang ditangkap GlobalExceptionHandler → otomatis return 404
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    // PUT /api/v1/customers/{id}
    // Yang boleh: ADMIN, STAFF
    @Operation(summary = "Update Customer")
    @ApiResponse(responseCode = "200", description = "Berhasil diupdate")
    @ApiResponse(responseCode = "401", description = "Token tidak valid",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Tidak punya akses",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomerById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody CreateCustomerRequest entity) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        CustomerResponse response = customerService.updateCustomer(id, entity);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/v1/customers/{id}
    // Yang boleh: ADMIN saja
    @Operation(summary = "Delete Customer")
    @ApiResponse(responseCode = "200", description = "Berhasil dihapus")
    @ApiResponse(responseCode = "401", description = "Token tidak valid",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Tidak punya akses",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        CustomerResponse response = customerService.deleteCustomer(id);
        return ResponseEntity.ok(response);
    }

    // PATCH /api/v1/customers/{id}
    // Yang boleh: ADMIN, STAFF
    @Operation(summary = "Patch Customer")
    @ApiResponse(responseCode = "200", description = "Berhasil dipatch")
    @ApiResponse(responseCode = "401", description = "Token tidak valid",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Tidak punya akses",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchCustomerById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody PatchCustomerRequest broski) {

        User user = resolveUser(authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        if (!RoleValidator.hasRole(user.getRole(), "ADMIN", "STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.error("FORBIDDEN", "You do not have permission to access this resource", null));
        }

        CustomerResponse response = customerService.patchCustomer(id, broski);
        return ResponseEntity.ok(response);
    }
}