package com.fif.finance_training;

import com.fif.finance_training.dto.RepaymentScheduleResponse;
import com.fif.finance_training.entity.LoanApplicationEntity;
import com.fif.finance_training.entity.RepaymentScheduleEntity;
import com.fif.finance_training.entity.enums.RepaymentStatus;
import com.fif.finance_training.exception.RepaymentScheduleNotFoundException;
import com.fif.finance_training.repository.RepaymentScheduleRepository;
import com.fif.finance_training.service.RepaymentScheduleService;
import com.fif.finance_training.web.StructuredLogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepaymentScheduleServiceMockTest {

    @Mock
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @Mock
    private StructuredLogger logger;

    @InjectMocks
    private RepaymentScheduleService repaymentScheduleService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(repaymentScheduleService, "annualInterestRate", new BigDecimal("0.12"));
    }

    private LoanApplicationEntity createLoan() {
        return LoanApplicationEntity.builder()
                .id(1L)
                .loanAmount(new BigDecimal("12000000.00"))
                .tenorMonth(12)
                .build();
    }

    @Test
    void generateSchedulesForLoan_success() {
        LoanApplicationEntity loan = createLoan();
        when(repaymentScheduleRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        repaymentScheduleService.generateSchedulesForLoan(loan);

        ArgumentCaptor<List<RepaymentScheduleEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repaymentScheduleRepository, times(1)).saveAll(captor.capture());

        List<RepaymentScheduleEntity> saved = captor.getValue();
        assertEquals(12, saved.size());
        assertEquals(1, saved.get(0).getInstallmentNumber());
        assertEquals(12, saved.get(11).getInstallmentNumber());
        assertEquals(RepaymentStatus.UNPAID, saved.get(0).getStatus());

        // verify(logger, times(1)).info(eq("SCHEDULE_GENERATE_ATTEMPT"), anyString(), any());
        // verify(logger, times(1)).info(eq("SCHEDULE_GENERATED"), anyString(), any());
    }

    @Test
    void getSchedulesByLoanId_success() {
        LoanApplicationEntity loan = createLoan();
        RepaymentScheduleEntity schedule = RepaymentScheduleEntity.builder()
                .id(1L)
                .loanApplication(loan)
                .installmentNumber(1)
                .dueDate(LocalDate.now().plusMonths(1))
                .principalAmount(new BigDecimal("1000000.00"))
                .interestAmount(new BigDecimal("120000.00"))
                .totalAmount(new BigDecimal("1120000.00"))
                .status(RepaymentStatus.UNPAID)
                .build();

        when(repaymentScheduleRepository.findByLoanApplicationId(1L)).thenReturn(Arrays.asList(schedule));

        List<RepaymentScheduleResponse> responses = repaymentScheduleService.getSchedulesByLoanId(1L);

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(1, responses.get(0).getInstallmentNumber());
        assertNotNull(responses.get(0).getDueDate());
        assertEquals(new BigDecimal("1000000.00"), responses.get(0).getPrincipalAmount());
        assertEquals(new BigDecimal("120000.00"), responses.get(0).getInterestAmount());
        assertEquals(new BigDecimal("1120000.00"), responses.get(0).getTotalAmount());
        assertEquals("UNPAID", responses.get(0).getStatus());
    }

    @Test
    void getSchedulesByLoanIdAndStatus_success() {
        LoanApplicationEntity loan = createLoan();
        RepaymentScheduleEntity schedule = RepaymentScheduleEntity.builder()
                .id(1L)
                .loanApplication(loan)
                .installmentNumber(1)
                .dueDate(LocalDate.now().plusMonths(1))
                .principalAmount(new BigDecimal("1000000.00"))
                .interestAmount(new BigDecimal("120000.00"))
                .totalAmount(new BigDecimal("1120000.00"))
                .status(RepaymentStatus.PAID)
                .build();

        when(repaymentScheduleRepository.findByLoanApplicationIdAndStatus(1L, RepaymentStatus.PAID))
                .thenReturn(Arrays.asList(schedule));

        List<RepaymentScheduleResponse> responses = repaymentScheduleService.getSchedulesByLoanIdAndStatus(1L, "PAID");

        assertEquals(1, responses.size());
        assertEquals("PAID", responses.get(0).getStatus());
    }

    @Test
    void getScheduleById_success() {
        LoanApplicationEntity loan = createLoan();
        RepaymentScheduleEntity schedule = RepaymentScheduleEntity.builder()
                .id(1L)
                .loanApplication(loan)
                .installmentNumber(2)
                .dueDate(LocalDate.now().plusMonths(2))
                .principalAmount(new BigDecimal("1000000.00"))
                .interestAmount(new BigDecimal("120000.00"))
                .totalAmount(new BigDecimal("1120000.00"))
                .status(RepaymentStatus.UNPAID)
                .build();

        when(repaymentScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        RepaymentScheduleResponse response = repaymentScheduleService.getScheduleById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(2, response.getInstallmentNumber());
        assertEquals(new BigDecimal("1000000.00"), response.getPrincipalAmount());
        assertEquals(new BigDecimal("120000.00"), response.getInterestAmount());
        assertEquals(new BigDecimal("1120000.00"), response.getTotalAmount());
        assertEquals("UNPAID", response.getStatus());
    }

    @Test
    void getScheduleById_notFound_throwsException() {
        when(repaymentScheduleRepository.findById(999L)).thenReturn(Optional.empty());

        RepaymentScheduleNotFoundException exception = assertThrows(
                RepaymentScheduleNotFoundException.class,
                () -> repaymentScheduleService.getScheduleById(999L)
        );
        assertEquals("Schedule not found with id: 999", exception.getMessage());
    }
}