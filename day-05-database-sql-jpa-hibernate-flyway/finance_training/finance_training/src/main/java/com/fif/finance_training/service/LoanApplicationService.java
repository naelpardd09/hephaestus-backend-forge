package com.fif.finance_training.service;

import com.fif.finance_training.dto.*;
import com.fif.finance_training.entity.CustomerEntity;
import com.fif.finance_training.entity.LoanApplicationEntity;
import com.fif.finance_training.entity.RepaymentScheduleEntity;
import com.fif.finance_training.entity.enums.LoanStatus;
import com.fif.finance_training.entity.enums.RepaymentStatus;
import com.fif.finance_training.exception.CustomerNotFoundException;
import com.fif.finance_training.exception.LoanApplicationNotFoundException;
import com.fif.finance_training.web.StructuredLogger;
import com.fif.finance_training.repository.CustomerRepository;
import com.fif.finance_training.repository.LoanApplicationRepository;
import com.fif.finance_training.repository.RepaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomerRepository customerRepository;
    private final RepaymentScheduleService repaymentScheduleService;
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final StructuredLogger logger;

    @Transactional
    public LoanApplicationResponse createLoan(CreateLoanApplicationRequest request) {
        logger.info("LOAN_CREATE_ATTEMPT", "Creating new loan application",
                "customerId", request.getCustomerId().toString(),
                "loanAmount", request.getLoanAmount().toString(),
                "tenorMonth", request.getTenorMonth().toString());

        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> {
                    logger.warn("LOAN_CREATE_FAILED", "Customer not found for loan",
                            "customerId", request.getCustomerId().toString());
                    return new CustomerNotFoundException("Customer not found with id: " + request.getCustomerId());
                });

        LoanApplicationEntity entity = LoanApplicationEntity.builder()
                .customer(customer)
                .loanAmount(request.getLoanAmount())
                .tenorMonth(request.getTenorMonth())
                .purpose(request.getPurpose())
                .status(LoanStatus.SUBMITTED)
                .build();

        LoanApplicationEntity saved = loanApplicationRepository.save(entity);
        
        logger.info("LOAN_SUBMITTED", "Loan application submitted successfully",
                "loanId", saved.getId().toString(),
                "customerId", customer.getId().toString(),
                "loanAmount", saved.getLoanAmount().toString(),
                "status", saved.getStatus().name());

        return mapToResponse(saved);
    }

    @Transactional
    public LoanApplicationResponse updateLoanStatus(Long id, UpdateLoanStatusRequest request) {
        logger.info("LOAN_STATUS_UPDATE_ATTEMPT", "Updating loan status",
                "loanId", id.toString(),
                "requestedStatus", request.getStatus());

        LoanApplicationEntity loan = loanApplicationRepository.findByIdWithCustomer(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Loan not found with id: " + id));

        LoanStatus currentStatus = loan.getStatus();
        
        LoanStatus newStatus;
        try {
            newStatus = LoanStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("LOAN_STATUS_INVALID", "Invalid status value",
                    "loanId", id.toString(),
                    "requestedStatus", request.getStatus());
            throw new IllegalArgumentException("Invalid status value: " + request.getStatus());
        }

        validateStatusTransition(currentStatus, newStatus, loan);

        loan.setStatus(newStatus);
        
        if (currentStatus == LoanStatus.APPROVED && newStatus == LoanStatus.DISBURSED) {
            List<RepaymentScheduleEntity> existing = repaymentScheduleRepository.findByLoanApplicationId(loan.getId());
            if (existing.isEmpty()) {
                repaymentScheduleService.generateSchedulesForLoan(loan);
                logger.info("LOAN_DISBURSED", "Loan disbursed, repayment schedules generated",
                        "loanId", id.toString(),
                        "tenorMonth", loan.getTenorMonth().toString());
            }
        }

        if (newStatus == LoanStatus.APPROVED) {
            logger.info("LOAN_APPROVED", "Loan application approved",
                    "loanId", id.toString(),
                    "customerId", loan.getCustomer().getId().toString());
        } else if (newStatus == LoanStatus.REJECTED) {
            logger.info("LOAN_REJECTED", "Loan application rejected",
                    "loanId", id.toString(),
                    "customerId", loan.getCustomer().getId().toString());
        }

        LoanApplicationEntity updated = loanApplicationRepository.save(loan);
        return mapToResponse(updated);
    }

    private void validateStatusTransition(LoanStatus currentStatus, LoanStatus newStatus, LoanApplicationEntity loan) {
        boolean isValid = false;
        
        switch (currentStatus) {
            case SUBMITTED:
                isValid = newStatus == LoanStatus.APPROVED || newStatus == LoanStatus.REJECTED;
                break;
            case APPROVED:
                isValid = newStatus == LoanStatus.DISBURSED;
                break;
            case DISBURSED:
                if (newStatus == LoanStatus.CLOSED) {
                    List<RepaymentScheduleEntity> schedules = repaymentScheduleRepository.findByLoanApplicationId(loan.getId());
                    boolean allPaid = schedules.stream().allMatch(s -> s.getStatus() == RepaymentStatus.PAID);
                    if (!allPaid) {
                        logger.warn("LOAN_CLOSE_FAILED", "Cannot close loan, not all schedules paid",
                                "loanId", loan.getId().toString(),
                                "totalSchedules", String.valueOf(schedules.size()));
                        throw new IllegalArgumentException("Cannot close loan. Not all repayment schedules are PAID.");
                    }
                    isValid = true;
                }
                break;
            case REJECTED:
            case CLOSED:
                throw new IllegalArgumentException("Status is final. Cannot be changed.");
        }

        if (!isValid) {
            logger.warn("LOAN_STATUS_TRANSITION_INVALID", "Invalid status transition",
                    "currentStatus", currentStatus.name(),
                    "newStatus", newStatus.name());
            throw new IllegalArgumentException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    @Transactional(readOnly = true)
    public LoanApplicationResponse getLoanById(Long id) {
        LoanApplicationEntity entity = loanApplicationRepository.findByIdWithCustomer(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Loan not found with id: " + id));
        return mapToResponse(entity);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LoanApplicationResponse> getAllLoans(Pageable pageable) {
        Page<LoanApplicationEntity> page = loanApplicationRepository.findAll(pageable);
        return mapToPagedResponse(page);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LoanApplicationResponse> getLoansByStatus(String status, Pageable pageable) {
        LoanStatus loanStatus = LoanStatus.valueOf(status.toUpperCase());
        Page<LoanApplicationEntity> page = loanApplicationRepository.findByStatus(loanStatus, pageable);
        return mapToPagedResponse(page);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LoanApplicationResponse> getLoansByDateRange(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable) {
        Page<LoanApplicationEntity> page = loanApplicationRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        return mapToPagedResponse(page);
    }

    @Transactional(readOnly = true)
    public List<LoanApplicationResponse> getLoansByCustomerId(Long customerId) {
        return loanApplicationRepository.findLoansByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoanStatusSummaryResponse> getLoanStatusSummary() {
        List<Object[]> results = loanApplicationRepository.getLoanStatusSummaryRaw();
        return results.stream()
                .map(row -> new LoanStatusSummaryResponse(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        (BigDecimal) row[2]
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerOutstandingResponse> getCustomerOutstanding() {
        List<Object[]> results = loanApplicationRepository.getCustomerOutstandingRaw();
        return results.stream()
                .map(row -> {
                    BigDecimal totalLoan = (BigDecimal) row[3];
                    BigDecimal totalPaid = (BigDecimal) row[4];
                    BigDecimal outstanding = totalLoan.subtract(totalPaid);
                    return new CustomerOutstandingResponse(
                            ((Number) row[0]).longValue(),
                            (String) row[1],
                            (String) row[2],
                            totalLoan,
                            totalPaid,
                            outstanding
                    );
                })
                .collect(Collectors.toList());
    }

    private PagedResponse<LoanApplicationResponse> mapToPagedResponse(Page<LoanApplicationEntity> page) {
        List<LoanApplicationResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PagedResponse.<LoanApplicationResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private LoanApplicationResponse mapToResponse(LoanApplicationEntity entity) {
        LoanApplicationResponse.CustomerDetail customerDetail = LoanApplicationResponse.CustomerDetail.builder()
                .id(entity.getCustomer().getId())
                .fullName(entity.getCustomer().getFullName())
                .nik(entity.getCustomer().getNik())
                .email(entity.getCustomer().getEmail())
                .build();

        return LoanApplicationResponse.builder()
                .id(entity.getId())
                .loanAmount(entity.getLoanAmount())
                .tenorMonth(entity.getTenorMonth())
                .purpose(entity.getPurpose())
                .status(entity.getStatus().name())
                .customer(customerDetail)
                .build();
    }
}