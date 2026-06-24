package com.fif.finance_training.service;

import com.fif.finance_training.dto.CreatePaymentTransactionRequest;
import com.fif.finance_training.dto.PaymentTransactionResponse;
import com.fif.finance_training.entity.PaymentTransactionEntity;
import com.fif.finance_training.entity.RepaymentScheduleEntity;
import com.fif.finance_training.entity.enums.PaymentStatus;
import com.fif.finance_training.entity.enums.RepaymentStatus;
import com.fif.finance_training.exception.RepaymentScheduleNotFoundException;
import com.fif.finance_training.web.StructuredLogger;
import com.fif.finance_training.repository.PaymentTransactionRepository;
import com.fif.finance_training.repository.RepaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final StructuredLogger logger;

    @Transactional
    public PaymentTransactionResponse createPayment(CreatePaymentTransactionRequest request) {
        logger.info("PAYMENT_CREATE_ATTEMPT", "Processing payment",
                "scheduleId", request.getRepaymentScheduleId().toString(),
                "paidAmount", request.getPaidAmount().toString(),
                "reference", request.getPaymentReference());

        RepaymentScheduleEntity schedule = repaymentScheduleRepository.findById(request.getRepaymentScheduleId())
                .orElseThrow(() -> {
                    logger.warn("PAYMENT_FAILED", "Repayment schedule not found",
                            "scheduleId", request.getRepaymentScheduleId().toString());
                    return new RepaymentScheduleNotFoundException("Repayment schedule not found with id: " + request.getRepaymentScheduleId());
                });

        PaymentTransactionEntity transaction = PaymentTransactionEntity.builder()
                .repaymentSchedule(schedule)
                .paymentReference(request.getPaymentReference())
                .paidAmount(request.getPaidAmount())
                .paidAt(request.getPaidAt() != null ? request.getPaidAt() : ZonedDateTime.now())
                .status(PaymentStatus.SUCCESS)
                .build();

        PaymentTransactionEntity saved = paymentTransactionRepository.save(transaction);

        BigDecimal totalPaid = paymentTransactionRepository.sumPaidAmountByScheduleId(schedule.getId());
        
        boolean fullyPaid = totalPaid != null && totalPaid.compareTo(schedule.getTotalAmount()) >= 0;
        if (fullyPaid) {
            schedule.setStatus(RepaymentStatus.PAID);
            repaymentScheduleRepository.save(schedule);
            logger.info("SCHEDULE_PAID", "Repayment schedule fully paid",
                    "scheduleId", schedule.getId().toString(),
                    "totalPaid", totalPaid.toString(),
                    "loanId", schedule.getLoanApplication().getId().toString());
        }

        logger.info("PAYMENT_SUCCESS", "Payment processed successfully",
                "transactionId", saved.getId().toString(),
                "scheduleId", schedule.getId().toString(),
                "paidAmount", saved.getPaidAmount().toString(),
                "scheduleFullyPaid", String.valueOf(fullyPaid));

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PaymentTransactionResponse> getPaymentsByScheduleId(Long scheduleId) {
        return paymentTransactionRepository.findByRepaymentScheduleId(scheduleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentTransactionResponse mapToResponse(PaymentTransactionEntity entity) {
        return PaymentTransactionResponse.builder()
                .id(entity.getId())
                .repaymentScheduleId(entity.getRepaymentSchedule().getId())
                .paymentReference(entity.getPaymentReference())
                .paidAmount(entity.getPaidAmount())
                .paidAt(entity.getPaidAt())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}