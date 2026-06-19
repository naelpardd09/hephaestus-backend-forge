package com.example.training.service;

import com.example.training.dto.CreateLoanApplicationRequest;
import com.example.training.dto.LoanApplicationResponse;
import com.example.training.model.LoanApplication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LoanApplicationService {

    private final Map<Long, LoanApplication> loanStorage = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private static final BigDecimal MANAGER_APPROVAL_MINIMUM = new BigDecimal("999999999");

    public LoanApplicationResponse createLoanApplication(CreateLoanApplicationRequest request) {
        Long id = idCounter.getAndIncrement();

        LoanApplication loan = new LoanApplication(
                id,
                request.getCustomerId(),
                request.getLoanAmount(),
                request.getTenorMonth(),
                request.getPurpose(),
                "SUBMITTED"
        );

        loanStorage.put(id, loan);
        return toResponse(loan);
    }

    public List<LoanApplicationResponse> getAllLoanApplications(String status, Long customerId) {
        List<LoanApplicationResponse> result = new ArrayList<>();

        for (LoanApplication loan : loanStorage.values()) {
            if (status != null && !loan.getStatus().equalsIgnoreCase(status)) {
                continue;
            }
            if (customerId != null && !loan.getCustomerId().equals(customerId)) {
                continue;
            }
            result.add(toResponse(loan));
        }

        return result;
    }

    public LoanApplicationResponse getLoanApplicationById(Long id) {
        LoanApplication loan = loanStorage.get(id);
        if (loan == null) return null;
        return toResponse(loan);
    }

    public LoanApplicationResponse approveLoanApplication(Long id) {
        LoanApplication loan = loanStorage.get(id);
        if (loan == null) return null;
        loan.setStatus("APPROVED");
        return toResponse(loan);
    }

    //approve khusus manager >999jt
    //jadi supaya 999jt keatas -> di-approve manager
    public String approveByManager(Long id) {
        LoanApplication loan = loanStorage.get(id);

        if (loan == null) {
            return "NOT_FOUND";
        }
        if(loan.getLoanAmount().compareTo(MANAGER_APPROVAL_MINIMUM)<=0){
            return "BELOW_MINIMUM";
        }
        loan.setStatus("APPROVED");
        return null;
    }

    public LoanApplicationResponse rejectLoanApplication(Long id) {
        LoanApplication loan = loanStorage.get(id);
        if (loan == null) return null;
        loan.setStatus("REJECTED");
        return toResponse(loan);
    }

    // Cancel hanya boleh dilakukan kalau status masih SUBMITTED.
    // Kalau sudah APPROVED atau REJECTED, tidak bisa dicancel — return null
    // dengan alasan berbeda, jadi kita pakai String sebagai sinyal ke controller.
    // Kalau loan tidak ditemukan → return "NOT_FOUND"
    // Kalau status bukan SUBMITTED → return "INVALID_STATUS"
    // Kalau berhasil → return null dan loan sudah terupdate (kita cek via getLoanApplicationById)
    public String cancelLoanApplication(Long id) {
        LoanApplication loan = loanStorage.get(id);

        if (loan == null) {
            return "NOT_FOUND";
        }

        // Loan yang sudah diproses tidak bisa dicancel
        if (!loan.getStatus().equals("SUBMITTED")) {
            return "INVALID_STATUS";
        }

        loan.setStatus("CANCELLED");
        return null; // null artinya sukses
    }

    // Getter terpisah untuk ambil response setelah cancel berhasil
    // dipanggil controller setelah cancelLoanApplication return null (sukses)
    public LoanApplicationResponse getCancelledLoan(Long id) {
        return toResponse(loanStorage.get(id));
    }

    private LoanApplicationResponse toResponse(LoanApplication loan) {
        return new LoanApplicationResponse(
                loan.getId(),
                loan.getCustomerId(),
                loan.getLoanAmount(),
                loan.getTenorMonth(),
                loan.getPurpose(),
                loan.getStatus()
        );
    }
}