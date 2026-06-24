package com.fif.finance_training;

import com.fif.finance_training.dto.CreatePaymentTransactionRequest;
import com.fif.finance_training.dto.PaymentTransactionResponse;
import com.fif.finance_training.entity.LoanApplicationEntity;
import com.fif.finance_training.entity.PaymentTransactionEntity;
import com.fif.finance_training.entity.RepaymentScheduleEntity;
import com.fif.finance_training.entity.enums.PaymentStatus;
import com.fif.finance_training.entity.enums.RepaymentStatus;
import com.fif.finance_training.exception.RepaymentScheduleNotFoundException;
import com.fif.finance_training.repository.PaymentTransactionRepository;
import com.fif.finance_training.repository.RepaymentScheduleRepository;
import com.fif.finance_training.service.PaymentTransactionService;
import com.fif.finance_training.web.StructuredLogger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceMockTest {

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @Mock
    private StructuredLogger logger;

    @InjectMocks
    private PaymentTransactionService paymentTransactionService;

    private RepaymentScheduleEntity createSchedule() {
        LoanApplicationEntity loan = LoanApplicationEntity.builder().id(1L).build();
        return RepaymentScheduleEntity.builder()
                .id(1L)
                .loanApplication(loan)
                .totalAmount(new BigDecimal("1000000.00"))
                .status(RepaymentStatus.UNPAID)
                .build();
    }

    @Test
    void createPayment_success_notFullyPaid() {
        RepaymentScheduleEntity schedule = createSchedule();
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(1L);
        request.setPaidAmount(new BigDecimal("500000.00"));
        request.setPaymentReference("REF-001");
        request.setPaidAt(ZonedDateTime.now());

        PaymentTransactionEntity saved = PaymentTransactionEntity.builder()
                .id(1L)
                .repaymentSchedule(schedule)
                .paymentReference("REF-001")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(request.getPaidAt())
                .status(PaymentStatus.SUCCESS)
                .createdAt(ZonedDateTime.now())
                .build();

        when(repaymentScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(paymentTransactionRepository.save(any(PaymentTransactionEntity.class))).thenReturn(saved);
        when(paymentTransactionRepository.sumPaidAmountByScheduleId(1L)).thenReturn(new BigDecimal("500000.00"));

        PaymentTransactionResponse response = paymentTransactionService.createPayment(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getRepaymentScheduleId());
        assertEquals("REF-001", response.getPaymentReference());
        assertEquals(new BigDecimal("500000.00"), response.getPaidAmount());
        assertNotNull(response.getPaidAt());
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getCreatedAt());

        assertEquals(RepaymentStatus.UNPAID, schedule.getStatus());
        verify(repaymentScheduleRepository, never()).save(any());
        // verify(logger, times(1)).info(eq("PAYMENT_CREATE_ATTEMPT"), anyString(), any());
        // verify(logger, times(1)).info(eq("PAYMENT_SUCCESS"), anyString(), any());
    }

    @Test
    void createPayment_success_fullyPaid() {
        RepaymentScheduleEntity schedule = createSchedule();
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(1L);
        request.setPaidAmount(new BigDecimal("1000000.00"));
        request.setPaymentReference("REF-002");
        request.setPaidAt(ZonedDateTime.now());

        PaymentTransactionEntity saved = PaymentTransactionEntity.builder()
                .id(2L)
                .repaymentSchedule(schedule)
                .paymentReference("REF-002")
                .paidAmount(new BigDecimal("1000000.00"))
                .paidAt(request.getPaidAt())
                .status(PaymentStatus.SUCCESS)
                .createdAt(ZonedDateTime.now())
                .build();

        when(repaymentScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(paymentTransactionRepository.save(any(PaymentTransactionEntity.class))).thenReturn(saved);
        when(paymentTransactionRepository.sumPaidAmountByScheduleId(1L)).thenReturn(new BigDecimal("1000000.00"));
        when(repaymentScheduleRepository.save(any(RepaymentScheduleEntity.class))).thenReturn(schedule);

        PaymentTransactionResponse response = paymentTransactionService.createPayment(request);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(RepaymentStatus.PAID, schedule.getStatus());
        // verify(repaymentScheduleRepository, times(1)).save(schedule);
        // verify(logger, times(1)).info(eq("SCHEDULE_PAID"), anyString(), any());
    }

    @Test
    void createPayment_totalPaidNull_notFullyPaid() {
        RepaymentScheduleEntity schedule = createSchedule();
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(1L);
        request.setPaidAmount(new BigDecimal("500000.00"));
        request.setPaymentReference("REF-003");
        request.setPaidAt(ZonedDateTime.now());

        PaymentTransactionEntity saved = PaymentTransactionEntity.builder()
                .id(3L)
                .repaymentSchedule(schedule)
                .paymentReference("REF-003")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(request.getPaidAt())
                .status(PaymentStatus.SUCCESS)
                .createdAt(ZonedDateTime.now())
                .build();

        when(repaymentScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(paymentTransactionRepository.save(any(PaymentTransactionEntity.class))).thenReturn(saved);
        when(paymentTransactionRepository.sumPaidAmountByScheduleId(1L)).thenReturn(null);

        PaymentTransactionResponse response = paymentTransactionService.createPayment(request);

        assertNotNull(response);
        assertEquals(RepaymentStatus.UNPAID, schedule.getStatus());
        verify(repaymentScheduleRepository, never()).save(any());
    }

    @Test
    void createPayment_nullPaidAt_usesNow() {
        RepaymentScheduleEntity schedule = createSchedule();
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(1L);
        request.setPaidAmount(new BigDecimal("500000.00"));
        request.setPaymentReference("REF-004");
        request.setPaidAt(null);

        PaymentTransactionEntity saved = PaymentTransactionEntity.builder()
                .id(4L)
                .repaymentSchedule(schedule)
                .paymentReference("REF-004")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(ZonedDateTime.now())
                .status(PaymentStatus.SUCCESS)
                .createdAt(ZonedDateTime.now())
                .build();

        when(repaymentScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(paymentTransactionRepository.save(any(PaymentTransactionEntity.class))).thenReturn(saved);
        when(paymentTransactionRepository.sumPaidAmountByScheduleId(1L)).thenReturn(new BigDecimal("500000.00"));

        PaymentTransactionResponse response = paymentTransactionService.createPayment(request);

        assertNotNull(response);
        verify(paymentTransactionRepository, times(1)).save(any(PaymentTransactionEntity.class));
    }

    @Test
    void createPayment_scheduleNotFound_throwsException() {
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(999L);
        request.setPaidAmount(new BigDecimal("500000.00"));
        request.setPaymentReference("REF-005");

        when(repaymentScheduleRepository.findById(999L)).thenReturn(Optional.empty());

        RepaymentScheduleNotFoundException exception = assertThrows(
                RepaymentScheduleNotFoundException.class,
                () -> paymentTransactionService.createPayment(request)
        );
        assertEquals("Repayment schedule not found with id: 999", exception.getMessage());
        verify(paymentTransactionRepository, never()).save(any());
        // verify(logger, times(1)).warn(eq("PAYMENT_FAILED"), anyString(), any());
    }

    @Test
    void getPaymentsByScheduleId_success() {
        RepaymentScheduleEntity schedule = createSchedule();
        PaymentTransactionEntity tx1 = PaymentTransactionEntity.builder()
                .id(1L)
                .repaymentSchedule(schedule)
                .paymentReference("REF-001")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(ZonedDateTime.now())
                .status(PaymentStatus.SUCCESS)
                .createdAt(ZonedDateTime.now())
                .build();
        PaymentTransactionEntity tx2 = PaymentTransactionEntity.builder()
                .id(2L)
                .repaymentSchedule(schedule)
                .paymentReference("REF-002")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(ZonedDateTime.now())
                .status(PaymentStatus.SUCCESS)
                .createdAt(ZonedDateTime.now())
                .build();

        when(paymentTransactionRepository.findByRepaymentScheduleId(1L)).thenReturn(Arrays.asList(tx1, tx2));

        List<PaymentTransactionResponse> responses = paymentTransactionService.getPaymentsByScheduleId(1L);

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("REF-001", responses.get(0).getPaymentReference());
        assertEquals(new BigDecimal("500000.00"), responses.get(0).getPaidAmount());
        assertEquals("SUCCESS", responses.get(0).getStatus());
        assertEquals(2L, responses.get(1).getId());
        assertEquals("REF-002", responses.get(1).getPaymentReference());
    }
}