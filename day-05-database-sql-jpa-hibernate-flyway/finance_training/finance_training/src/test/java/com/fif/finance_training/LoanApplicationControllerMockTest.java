package com.fif.finance_training;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fif.finance_training.dto.*;
import com.fif.finance_training.exception.LoanApplicationNotFoundException;
import com.fif.finance_training.service.LoanApplicationService;
import com.fif.finance_training.web.StructuredLogger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoanApplicationService loanApplicationService;

    @MockitoBean
    private StructuredLogger logger;

    private LoanApplicationResponse createLoanResponse(String status) {
        LoanApplicationResponse.CustomerDetail customer = LoanApplicationResponse.CustomerDetail.builder()
                .id(1L).fullName("Budi Santoso").nik("3173010101900001").email("budi@mail.com").build();
        return LoanApplicationResponse.builder()
                .id(1L)
                .loanAmount(new BigDecimal("10000000.00"))
                .tenorMonth(12)
                .purpose("Business Capital")
                .status(status)
                .customer(customer)
                .build();
    }

    @Test
    void createLoan_success() throws Exception {
        CreateLoanApplicationRequest request = new CreateLoanApplicationRequest();
        request.setCustomerId(1L);
        request.setLoanAmount(new BigDecimal("10000000.00"));
        request.setTenorMonth(12);
        request.setPurpose("Business Capital");

        LoanApplicationResponse response = createLoanResponse("SUBMITTED");

        when(loanApplicationService.createLoan(any(CreateLoanApplicationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/loan-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Loan application submitted successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.loan_amount").value(10000000.00))
                .andExpect(jsonPath("$.data.tenor_month").value(12))
                .andExpect(jsonPath("$.data.purpose").value("Business Capital"))
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.data.customer.id").value(1))
                .andExpect(jsonPath("$.data.customer.full_name").value("Budi Santoso"))
                .andExpect(jsonPath("$.data.customer.nik").value("3173010101900001"))
                .andExpect(jsonPath("$.data.customer.email").value("budi@mail.com"));

        verify(loanApplicationService, times(1)).createLoan(any(CreateLoanApplicationRequest.class));
    }

    @Test
    void createLoan_validationError() throws Exception {
        CreateLoanApplicationRequest request = new CreateLoanApplicationRequest();
        request.setCustomerId(null);
        request.setLoanAmount(new BigDecimal("-100"));
        request.setTenorMonth(0);
        request.setPurpose("");

        mockMvc.perform(post("/api/v1/loan-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(loanApplicationService, never()).createLoan(any());
    }

    @Test
    void getLoanById_success() throws Exception {
        LoanApplicationResponse response = createLoanResponse("APPROVED");

        when(loanApplicationService.getLoanById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/loan-applications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.customer.full_name").value("Budi Santoso"));

        verify(loanApplicationService, times(1)).getLoanById(1L);
    }

    @Test
    void getLoanById_notFound() throws Exception {
        when(loanApplicationService.getLoanById(999L)).thenThrow(new LoanApplicationNotFoundException("Loan not found with id: 999"));

        mockMvc.perform(get("/api/v1/loan-applications/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("LOAN_APPLICATION_NOT_FOUND"));

        verify(loanApplicationService, times(1)).getLoanById(999L);
    }

    @Test
    void getAllLoans_success() throws Exception {
        LoanApplicationResponse loan = createLoanResponse("SUBMITTED");
        PagedResponse<LoanApplicationResponse> pagedResponse = PagedResponse.<LoanApplicationResponse>builder()
                .content(Arrays.asList(loan))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1L)
                .totalPages(1)
                .build();

        when(loanApplicationService.getAllLoans(any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/loan-applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.page_number").value(0))
                .andExpect(jsonPath("$.data.page_size").value(10))
                .andExpect(jsonPath("$.data.total_elements").value(1))
                .andExpect(jsonPath("$.data.total_pages").value(1))
                .andExpect(jsonPath("$.data.content[0].status").value("SUBMITTED"));

        verify(loanApplicationService, times(1)).getAllLoans(any(Pageable.class));
    }

    @Test
    void getAllLoans_byStatus() throws Exception {
        LoanApplicationResponse loan = createLoanResponse("APPROVED");
        PagedResponse<LoanApplicationResponse> pagedResponse = PagedResponse.<LoanApplicationResponse>builder()
                .content(Arrays.asList(loan))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1L)
                .totalPages(1)
                .build();

        when(loanApplicationService.getLoansByStatus(eq("APPROVED"), any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/loan-applications").param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].status").value("APPROVED"));

        verify(loanApplicationService, times(1)).getLoansByStatus(eq("APPROVED"), any(Pageable.class));
    }

    @Test
    void getAllLoans_byDateRange() throws Exception {
        LoanApplicationResponse loan = createLoanResponse("SUBMITTED");
        PagedResponse<LoanApplicationResponse> pagedResponse = PagedResponse.<LoanApplicationResponse>builder()
                .content(Arrays.asList(loan))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1L)
                .totalPages(1)
                .build();

        ZonedDateTime start = ZonedDateTime.now().minusDays(7);
        ZonedDateTime end = ZonedDateTime.now();

        when(loanApplicationService.getLoansByDateRange(eq(start), eq(end), any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/loan-applications")
                        .param("start_date", start.toString())
                        .param("end_date", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].status").value("SUBMITTED"));

        verify(loanApplicationService, times(1)).getLoansByDateRange(eq(start), eq(end), any(Pageable.class));
    }

    @Test
    void getLoansByCustomerId_success() throws Exception {
        LoanApplicationResponse loan = createLoanResponse("SUBMITTED");

        when(loanApplicationService.getLoansByCustomerId(1L)).thenReturn(Arrays.asList(loan));

        mockMvc.perform(get("/api/v1/customers/1/loan-applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].status").value("SUBMITTED"))
                .andExpect(jsonPath("$.data[0].customer.full_name").value("Budi Santoso"));

        verify(loanApplicationService, times(1)).getLoansByCustomerId(1L);
    }

    @Test
    void updateLoanStatus_success() throws Exception {
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("APPROVED");

        LoanApplicationResponse response = createLoanResponse("APPROVED");

        when(loanApplicationService.updateLoanStatus(eq(1L), any(UpdateLoanStatusRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/loan-applications/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Loan application status updated successfully"))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        verify(loanApplicationService, times(1)).updateLoanStatus(eq(1L), any(UpdateLoanStatusRequest.class));
    }

    @Test
    void updateLoanStatus_validationError() throws Exception {
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setStatus("");

        mockMvc.perform(patch("/api/v1/loan-applications/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(loanApplicationService, never()).updateLoanStatus(any(), any());
    }

    @Test
    void getLoanStatusSummary_success() throws Exception {
        LoanStatusSummaryResponse s1 = new LoanStatusSummaryResponse("SUBMITTED", 5L, new BigDecimal("50000000.00"));
        LoanStatusSummaryResponse s2 = new LoanStatusSummaryResponse("APPROVED", 3L, new BigDecimal("30000000.00"));

        when(loanApplicationService.getLoanStatusSummary()).thenReturn(Arrays.asList(s1, s2));

        mockMvc.perform(get("/api/v1/loan-applications/summary/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].status").value("SUBMITTED"))
                .andExpect(jsonPath("$.data[0].totalLoan").value(5))
                .andExpect(jsonPath("$.data[0].totalAmount").value(50000000.00))
                .andExpect(jsonPath("$.data[1].status").value("APPROVED"))
                .andExpect(jsonPath("$.data[1].totalLoan").value(3));

        verify(loanApplicationService, times(1)).getLoanStatusSummary();
    }

    @Test
    void getCustomerOutstanding_success() throws Exception {
        CustomerOutstandingResponse out = new CustomerOutstandingResponse(
                1L, "Budi Santoso", "3173010101900001",
                new BigDecimal("10000000.00"), new BigDecimal("6000000.00"), new BigDecimal("4000000.00"));

        when(loanApplicationService.getCustomerOutstanding()).thenReturn(Arrays.asList(out));

        mockMvc.perform(get("/api/v1/customers/outstanding"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].full_name").value("Budi Santoso"))
                .andExpect(jsonPath("$.data[0].nik").value("3173010101900001"))
                .andExpect(jsonPath("$.data[0].total_loan_amount").value(10000000.00))
                .andExpect(jsonPath("$.data[0].total_paid_amount").value(6000000.00))
                .andExpect(jsonPath("$.data[0].outstanding_amount").value(4000000.00));

        verify(loanApplicationService, times(1)).getCustomerOutstanding();
    }
}