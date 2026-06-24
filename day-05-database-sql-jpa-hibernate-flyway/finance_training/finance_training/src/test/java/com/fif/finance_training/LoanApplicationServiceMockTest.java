package com.fif.finance_training;

import com.fif.finance_training.dto.*;
import com.fif.finance_training.entity.CustomerEntity;
import com.fif.finance_training.entity.LoanApplicationEntity;
import com.fif.finance_training.entity.RepaymentScheduleEntity;
import com.fif.finance_training.entity.enums.LoanStatus;
import com.fif.finance_training.entity.enums.RepaymentStatus;
import com.fif.finance_training.exception.CustomerNotFoundException;
import com.fif.finance_training.exception.LoanApplicationNotFoundException;
import com.fif.finance_training.repository.CustomerRepository;
import com.fif.finance_training.repository.LoanApplicationRepository;
import com.fif.finance_training.repository.RepaymentScheduleRepository;
import com.fif.finance_training.service.LoanApplicationService;
import com.fif.finance_training.service.RepaymentScheduleService;
import com.fif.finance_training.web.StructuredLogger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceMockTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RepaymentScheduleService repaymentScheduleService;

    @Mock
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @Mock
    private StructuredLogger logger;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    private CustomerEntity createCustomer() {
        return CustomerEntity.builder()
                .id(1L)
                .fullName("Budi Santoso")
                .nik("3173010101900001")
                .email("budi@mail.com")
                .phoneNumber("08123456789")
                .build();
    }

    private LoanApplicationEntity createLoanEntity(LoanStatus status) {
        return LoanApplicationEntity.builder()
                .id(1L)
                .customer(createCustomer())
                .loanAmount(new BigDecimal("10000000.00"))
                .tenorMonth(12)
                .purpose("Business Capital")
                .status(status)
                .build();
    }

    private CreateLoanApplicationRequest createLoanRequest() {
        CreateLoanApplicationRequest request = new CreateLoanApplicationRequest();
        request.setCustomerId(1L);
        request.setLoanAmount(new BigDecimal("10000000.00"));
        request.setTenorMonth(12);
        request.setPurpose("Business Capital");
        return request;
    }

    @Test
    void createLoan_success() {
        CreateLoanApplicationRequest request = createLoanRequest();
        CustomerEntity customer = createCustomer();
        LoanApplicationEntity savedEntity = createLoanEntity(LoanStatus.SUBMITTED);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenReturn(savedEntity);

        LoanApplicationResponse response = loanApplicationService.createLoan(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(new BigDecimal("10000000.00"), response.getLoanAmount());
        assertEquals(12, response.getTenorMonth());
        assertEquals("Business Capital", response.getPurpose());
        assertEquals("SUBMITTED", response.getStatus());
        assertNotNull(response.getCustomer());
        assertEquals(1L, response.getCustomer().getId());
        assertEquals("Budi Santoso", response.getCustomer().getFullName());
        assertEquals("3173010101900001", response.getCustomer().getNik());
        assertEquals("budi@mail.com", response.getCustomer().getEmail());

        verify(customerRepository, times(1)).findById(1L);
        verify(loanApplicationRepository, times(1)).save(any(LoanApplicationEntity.class));
        // verify(logger, times(1)).info(eq("LOAN_CREATE_ATTEMPT"), anyString(), any());
        // verify(logger, times(1)).info(eq("LOAN_SUBMITTED"), anyString(), any());
    }

    @Test
    void createLoan_customerNotFound_throwsException() {
        CreateLoanApplicationRequest request = createLoanRequest();
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> loanApplicationService.createLoan(request)
        );
        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(loanApplicationRepository, never()).save(any());
        // verify(logger, times(1)).warn(eq("LOAN_CREATE_FAILED"), anyString(), any());
    }

    @Test
    void updateLoanStatus_submittedToApproved_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        LoanApplicationEntity updatedLoan = createLoanEntity(LoanStatus.APPROVED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("APPROVED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenReturn(updatedLoan);

        LoanApplicationResponse response = loanApplicationService.updateLoanStatus(1L, request);
        assertEquals("APPROVED", response.getStatus());
        // verify(logger, times(1)).info(eq("LOAN_STATUS_UPDATE_ATTEMPT"), anyString(), any());
        // verify(logger, times(1)).info(eq("LOAN_APPROVED"), anyString(), any());
    }

    @Test
    void updateLoanStatus_submittedToRejected_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        LoanApplicationEntity updatedLoan = createLoanEntity(LoanStatus.REJECTED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("REJECTED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenReturn(updatedLoan);

        LoanApplicationResponse response = loanApplicationService.updateLoanStatus(1L, request);
        assertEquals("REJECTED", response.getStatus());
        // verify(logger, times(1)).info(eq("LOAN_REJECTED"), anyString(), any());
    }

    @Test
    void updateLoanStatus_approvedToDisbursed_generatesSchedules_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.APPROVED);
        LoanApplicationEntity updatedLoan = createLoanEntity(LoanStatus.DISBURSED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("DISBURSED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));
        when(repaymentScheduleRepository.findByLoanApplicationId(1L)).thenReturn(Collections.emptyList());
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenReturn(updatedLoan);

        LoanApplicationResponse response = loanApplicationService.updateLoanStatus(1L, request);
        assertEquals("DISBURSED", response.getStatus());
        verify(repaymentScheduleService, times(1)).generateSchedulesForLoan(loan);
        // verify(logger, times(1)).info(eq("LOAN_DISBURSED"), anyString(), any());
    }

    @Test
    void updateLoanStatus_approvedToDisbursed_existingSchedules_noRegenerate() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.APPROVED);
        LoanApplicationEntity updatedLoan = createLoanEntity(LoanStatus.DISBURSED);
        RepaymentScheduleEntity existing = RepaymentScheduleEntity.builder().id(1L).build();
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("DISBURSED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));
        when(repaymentScheduleRepository.findByLoanApplicationId(1L)).thenReturn(Arrays.asList(existing));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenReturn(updatedLoan);

        LoanApplicationResponse response = loanApplicationService.updateLoanStatus(1L, request);
        assertEquals("DISBURSED", response.getStatus());
        verify(repaymentScheduleService, never()).generateSchedulesForLoan(any());
    }

    @Test
    void updateLoanStatus_disbursedToClosed_allPaid_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.DISBURSED);
        LoanApplicationEntity updatedLoan = createLoanEntity(LoanStatus.CLOSED);
        RepaymentScheduleEntity s1 = RepaymentScheduleEntity.builder().status(RepaymentStatus.PAID).build();
        RepaymentScheduleEntity s2 = RepaymentScheduleEntity.builder().status(RepaymentStatus.PAID).build();
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("CLOSED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));
        when(repaymentScheduleRepository.findByLoanApplicationId(1L)).thenReturn(Arrays.asList(s1, s2));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenReturn(updatedLoan);

        LoanApplicationResponse response = loanApplicationService.updateLoanStatus(1L, request);
        assertEquals("CLOSED", response.getStatus());
    }

    @Test
    void updateLoanStatus_disbursedToClosed_notAllPaid_throwsException() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.DISBURSED);
        RepaymentScheduleEntity s1 = RepaymentScheduleEntity.builder().status(RepaymentStatus.PAID).build();
        RepaymentScheduleEntity s2 = RepaymentScheduleEntity.builder().status(RepaymentStatus.UNPAID).build();
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("CLOSED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));
        when(repaymentScheduleRepository.findByLoanApplicationId(1L)).thenReturn(Arrays.asList(s1, s2));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.updateLoanStatus(1L, request)
        );
        assertEquals("Cannot close loan. Not all repayment schedules are PAID.", exception.getMessage());
        // verify(logger, times(1)).warn(eq("LOAN_CLOSE_FAILED"), anyString(), any());
    }

    @Test
    void updateLoanStatus_invalidTransition_submittedToDisbursed_throwsException() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("DISBURSED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.updateLoanStatus(1L, request)
        );
        assertEquals("Invalid status transition from SUBMITTED to DISBURSED", exception.getMessage());
        // verify(logger, times(1)).warn(eq("LOAN_STATUS_TRANSITION_INVALID"), anyString(), any());
    }

    @Test
    void updateLoanStatus_rejectedToAny_throwsException() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.REJECTED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("APPROVED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.updateLoanStatus(1L, request)
        );
        assertEquals("Status is final. Cannot be changed.", exception.getMessage());
    }

    @Test
    void updateLoanStatus_closedToAny_throwsException() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.CLOSED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("APPROVED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.updateLoanStatus(1L, request)
        );
        assertEquals("Status is final. Cannot be changed.", exception.getMessage());
    }

    @Test
    void updateLoanStatus_disbursedToNonClosed_throwsException() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.DISBURSED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("APPROVED");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.updateLoanStatus(1L, request)
        );
        assertEquals("Invalid status transition from DISBURSED to APPROVED", exception.getMessage());
    }

    @Test
    void updateLoanStatus_invalidStatusValue_throwsException() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("INVALID_STATUS");

        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.updateLoanStatus(1L, request)
        );
        assertEquals("Invalid status value: INVALID_STATUS", exception.getMessage());
        // verify(logger, times(1)).warn(eq("LOAN_STATUS_INVALID"), anyString(), any());
    }

    @Test
    void updateLoanStatus_loanNotFound_throwsException() {
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("APPROVED");
        when(loanApplicationRepository.findByIdWithCustomer(999L)).thenReturn(Optional.empty());

        LoanApplicationNotFoundException exception = assertThrows(
                LoanApplicationNotFoundException.class,
                () -> loanApplicationService.updateLoanStatus(999L, request)
        );
        assertEquals("Loan not found with id: 999", exception.getMessage());
    }

    @Test
    void getLoanById_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        when(loanApplicationRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(loan));

        LoanApplicationResponse response = loanApplicationService.getLoanById(1L);
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(new BigDecimal("10000000.00"), response.getLoanAmount());
        assertEquals(12, response.getTenorMonth());
        assertEquals("Business Capital", response.getPurpose());
        assertEquals("SUBMITTED", response.getStatus());
        assertNotNull(response.getCustomer());
        assertEquals(1L, response.getCustomer().getId());
        assertEquals("Budi Santoso", response.getCustomer().getFullName());
        assertEquals("3173010101900001", response.getCustomer().getNik());
        assertEquals("budi@mail.com", response.getCustomer().getEmail());
    }

    @Test
    void getLoanById_notFound_throwsException() {
        when(loanApplicationRepository.findByIdWithCustomer(999L)).thenReturn(Optional.empty());

        LoanApplicationNotFoundException exception = assertThrows(
                LoanApplicationNotFoundException.class,
                () -> loanApplicationService.getLoanById(999L)
        );
        assertEquals("Loan not found with id: 999", exception.getMessage());
    }

    @Test
    void getAllLoans_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        Page<LoanApplicationEntity> page = new PageImpl<>(Arrays.asList(loan), PageRequest.of(0, 10), 1);
        when(loanApplicationRepository.findAll(any(Pageable.class))).thenReturn(page);

        PagedResponse<LoanApplicationResponse> response = loanApplicationService.getAllLoans(PageRequest.of(0, 10));
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(0, response.getPageNumber());
        assertEquals(10, response.getPageSize());
        assertEquals(1L, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(1L, response.getContent().get(0).getId());
    }

    @Test
    void getLoansByStatus_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.APPROVED);
        Page<LoanApplicationEntity> page = new PageImpl<>(Arrays.asList(loan), PageRequest.of(0, 10), 1);
        when(loanApplicationRepository.findByStatus(LoanStatus.APPROVED, PageRequest.of(0, 10))).thenReturn(page);

        PagedResponse<LoanApplicationResponse> response = loanApplicationService.getLoansByStatus("APPROVED", PageRequest.of(0, 10));
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("APPROVED", response.getContent().get(0).getStatus());
    }

    @Test
    void getLoansByDateRange_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        Page<LoanApplicationEntity> page = new PageImpl<>(Arrays.asList(loan), PageRequest.of(0, 10), 1);
        ZonedDateTime start = ZonedDateTime.now().minusDays(7);
        ZonedDateTime end = ZonedDateTime.now();
        when(loanApplicationRepository.findByCreatedAtBetween(start, end, PageRequest.of(0, 10))).thenReturn(page);

        PagedResponse<LoanApplicationResponse> response = loanApplicationService.getLoansByDateRange(start, end, PageRequest.of(0, 10));
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
    }

    @Test
    void getLoansByCustomerId_success() {
        LoanApplicationEntity loan = createLoanEntity(LoanStatus.SUBMITTED);
        when(loanApplicationRepository.findLoansByCustomerId(1L)).thenReturn(Arrays.asList(loan));

        List<LoanApplicationResponse> responses = loanApplicationService.getLoansByCustomerId(1L);
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("SUBMITTED", responses.get(0).getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getLoanStatusSummary_success() {
        Object[] row1 = new Object[]{"SUBMITTED", 5L, new BigDecimal("50000000.00")};
        Object[] row2 = new Object[]{"APPROVED", 3L, new BigDecimal("30000000.00")};
        List<Object[]> results = Arrays.<Object[]>asList(row1, row2);
        when(loanApplicationRepository.getLoanStatusSummaryRaw()).thenReturn(results);

        List<LoanStatusSummaryResponse> responses = loanApplicationService.getLoanStatusSummary();
        assertEquals(2, responses.size());
        assertEquals("SUBMITTED", responses.get(0).getStatus());
        assertEquals(5L, responses.get(0).getTotalLoan());
        assertEquals(new BigDecimal("50000000.00"), responses.get(0).getTotalAmount());
        assertEquals("APPROVED", responses.get(1).getStatus());
        assertEquals(3L, responses.get(1).getTotalLoan());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getCustomerOutstanding_success() {
        Object[] row = new Object[]{
                1L, "Budi Santoso", "3173010101900001",
                new BigDecimal("10000000.00"), new BigDecimal("6000000.00")
        };
        List<Object[]> results = Arrays.<Object[]>asList(row);
        when(loanApplicationRepository.getCustomerOutstandingRaw()).thenReturn(results);

        List<CustomerOutstandingResponse> responses = loanApplicationService.getCustomerOutstanding();
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Budi Santoso", responses.get(0).getFullName());
        assertEquals("3173010101900001", responses.get(0).getNik());
        assertEquals(new BigDecimal("10000000.00"), responses.get(0).getTotalLoanAmount());
        assertEquals(new BigDecimal("6000000.00"), responses.get(0).getTotalPaidAmount());
        assertEquals(new BigDecimal("4000000.00"), responses.get(0).getOutstandingAmount());
    }
}