package com.fif.finance_training;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fif.finance_training.dto.ApiResponse;
import com.fif.finance_training.dto.RepaymentScheduleResponse;
import com.fif.finance_training.exception.RepaymentScheduleNotFoundException;
import com.fif.finance_training.service.RepaymentScheduleService;
import com.fif.finance_training.web.StructuredLogger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepaymentScheduleController.class)
class RepaymentScheduleControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RepaymentScheduleService repaymentScheduleService;

    @MockitoBean
    private StructuredLogger logger;

    private RepaymentScheduleResponse createScheduleResponse(String status) {
        return RepaymentScheduleResponse.builder()
                .id(1L)
                .installmentNumber(1)
                .dueDate(LocalDate.now().plusMonths(1))
                .principalAmount(new BigDecimal("1000000.00"))
                .interestAmount(new BigDecimal("120000.00"))
                .totalAmount(new BigDecimal("1120000.00"))
                .status(status)
                .build();
    }

    @Test
    void getSchedulesByLoanId_success() throws Exception {
        RepaymentScheduleResponse s1 = createScheduleResponse("UNPAID");
        RepaymentScheduleResponse s2 = RepaymentScheduleResponse.builder()
                .id(2L)
                .installmentNumber(2)
                .dueDate(LocalDate.now().plusMonths(2))
                .principalAmount(new BigDecimal("1000000.00"))
                .interestAmount(new BigDecimal("120000.00"))
                .totalAmount(new BigDecimal("1120000.00"))
                .status("UNPAID")
                .build();

        when(repaymentScheduleService.getSchedulesByLoanId(1L)).thenReturn(Arrays.asList(s1, s2));

        mockMvc.perform(get("/api/v1/loan-applications/1/repayment-schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Repayment schedules retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].installment_number").value(1))
                .andExpect(jsonPath("$.data[0].principal_amount").value(1000000.00))
                .andExpect(jsonPath("$.data[0].interest_amount").value(120000.00))
                .andExpect(jsonPath("$.data[0].total_amount").value(1120000.00))
                .andExpect(jsonPath("$.data[0].status").value("UNPAID"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].installment_number").value(2));

        verify(repaymentScheduleService, times(1)).getSchedulesByLoanId(1L);
        verify(repaymentScheduleService, never()).getSchedulesByLoanIdAndStatus(any(), any());
    }

    @Test
    void getSchedulesByLoanId_withStatusFilter() throws Exception {
        RepaymentScheduleResponse s1 = createScheduleResponse("PAID");

        when(repaymentScheduleService.getSchedulesByLoanIdAndStatus(1L, "PAID")).thenReturn(Arrays.asList(s1));

        mockMvc.perform(get("/api/v1/loan-applications/1/repayment-schedules")
                        .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].status").value("PAID"));

        verify(repaymentScheduleService, times(1)).getSchedulesByLoanIdAndStatus(1L, "PAID");
        verify(repaymentScheduleService, never()).getSchedulesByLoanId(any());
    }

    @Test
    void getScheduleById_success() throws Exception {
        RepaymentScheduleResponse response = createScheduleResponse("UNPAID");

        when(repaymentScheduleService.getScheduleById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/repayment-schedules/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Repayment schedule detail retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.installment_number").value(1))
                .andExpect(jsonPath("$.data.principal_amount").value(1000000.00))
                .andExpect(jsonPath("$.data.interest_amount").value(120000.00))
                .andExpect(jsonPath("$.data.total_amount").value(1120000.00))
                .andExpect(jsonPath("$.data.status").value("UNPAID"));

        verify(repaymentScheduleService, times(1)).getScheduleById(1L);
    }

    @Test
    void getScheduleById_notFound() throws Exception {
        when(repaymentScheduleService.getScheduleById(999L))
                .thenThrow(new RepaymentScheduleNotFoundException("Schedule not found with id: 999"));

        mockMvc.perform(get("/api/v1/repayment-schedules/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("REPAYMENT_SCHEDULE_NOT_FOUND"));

        verify(repaymentScheduleService, times(1)).getScheduleById(999L);
    }
}