package com.fif.finance_training.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fif.finance_training.entity.LoanApplicationEntity;
import com.fif.finance_training.entity.enums.LoanStatus;

import java.time.ZonedDateTime;

public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity, Long> {

    List<LoanApplicationEntity> findByCustomerId(Long customerId);

    List<LoanApplicationEntity> findByStatus(LoanStatus loanStatus);

    Page<LoanApplicationEntity> findByStatus(LoanStatus loanStatus, Pageable pageable);

    Page<LoanApplicationEntity> findByCreatedAtBetween(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable);

    @Query("SELECT l FROM LoanApplicationEntity l JOIN FETCH l.customer WHERE l.id = :id")
    Optional<LoanApplicationEntity> findByIdWithCustomer(@Param("id") Long id);

    @Query("SELECT l FROM LoanApplicationEntity l JOIN l.customer c WHERE c.id = :customerId")
    List<LoanApplicationEntity> findLoansByCustomerId(@Param("customerId") Long customerId);

    @Query(value = """
        SELECT l.status, COUNT(*), SUM(l.loan_amount)
        FROM loan_applications l
        GROUP BY l.status
        """, nativeQuery = true)
    List<Object[]> getLoanStatusSummaryRaw();

    @Query(value = """
        SELECT 
            c.id, c.full_name, c.nik,
            COALESCE(SUM(l.loan_amount), 0) as total_loan_amount,
            COALESCE(SUM(pt.paid_amount), 0) as total_paid_amount
        FROM customers c
        LEFT JOIN loan_applications l ON l.customer_id = c.id AND l.status = 'DISBURSED'
        LEFT JOIN repayment_schedules r ON r.loan_application_id = l.id
        LEFT JOIN payment_transactions pt ON pt.repayment_schedule_id = r.id AND pt.status = 'SUCCESS'
        WHERE c.deleted_at IS NULL
        GROUP BY c.id, c.full_name, c.nik
        """, nativeQuery = true)
    List<Object[]> getCustomerOutstandingRaw();
}