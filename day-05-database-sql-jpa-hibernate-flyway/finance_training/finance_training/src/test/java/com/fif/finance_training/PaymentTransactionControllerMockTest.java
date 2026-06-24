package com.fif.finance_training;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fif.finance_training.dto.*;
import com.fif.finance_training.exception.RepaymentScheduleNotFoundException;
import com.fif.finance_training.service.PaymentTransactionService;
import com.fif.finance_training.web.StructuredLogger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(PaymentTransactionController.class)
class PaymentTransactionControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentTransactionService paymentTransactionService;

    @MockitoBean
    private StructuredLogger logger;

    @Test
    void createPayment_success() throws Exception {
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(1L);
        request.setPaymentReference("REF-001");
        request.setPaidAmount(new BigDecimal("500000.00"));
        request.setPaidAt(ZonedDateTime.now());

        PaymentTransactionResponse response = PaymentTransactionResponse.builder()
                .id(1L)
                .repaymentScheduleId(1L)
                .paymentReference("REF-001")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(ZonedDateTime.now())
                .status("SUCCESS")
                .createdAt(ZonedDateTime.now())
                .build();

        when(paymentTransactionService.createPayment(any(CreatePaymentTransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/payment-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment processed successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.repayment_schedule_id").value(1))
                .andExpect(jsonPath("$.data.payment_reference").value("REF-001"))
                .andExpect(jsonPath("$.data.paid_amount").value(500000.00))
                .andExpect(jsonPath("$.data.status").value("SUCCESS"));

        verify(paymentTransactionService, times(1)).createPayment(any(CreatePaymentTransactionRequest.class));
    }

    @Test
    void createPayment_validationError() throws Exception {
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(null); // invalid
        request.setPaymentReference(""); // invalid
        request.setPaidAmount(new BigDecimal("-100")); // invalid
        request.setPaidAt(null); // invalid

        mockMvc.perform(post("/api/v1/payment-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(paymentTransactionService, never()).createPayment(any());
    }

    @Test
    void createPayment_scheduleNotFound() throws Exception {
        CreatePaymentTransactionRequest request = new CreatePaymentTransactionRequest();
        request.setRepaymentScheduleId(999L);
        request.setPaymentReference("REF-001");
        request.setPaidAmount(new BigDecimal("500000.00"));
        request.setPaidAt(ZonedDateTime.now());

        when(paymentTransactionService.createPayment(any(CreatePaymentTransactionRequest.class)))
                .thenThrow(new RepaymentScheduleNotFoundException("Repayment schedule not found with id: 999"));

        mockMvc.perform(post("/api/v1/payment-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("REPAYMENT_SCHEDULE_NOT_FOUND"));

        verify(paymentTransactionService, times(1)).createPayment(any(CreatePaymentTransactionRequest.class));
    }

    @Test
    void getPaymentsByScheduleId_success() throws Exception {
        PaymentTransactionResponse tx1 = PaymentTransactionResponse.builder()
                .id(1L)
                .repaymentScheduleId(1L)
                .paymentReference("REF-001")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(ZonedDateTime.now())
                .status("SUCCESS")
                .createdAt(ZonedDateTime.now())
                .build();

        PaymentTransactionResponse tx2 = PaymentTransactionResponse.builder()
                .id(2L)
                .repaymentScheduleId(1L)
                .paymentReference("REF-002")
                .paidAmount(new BigDecimal("500000.00"))
                .paidAt(ZonedDateTime.now())
                .status("SUCCESS")
                .createdAt(ZonedDateTime.now())
                .build();

        when(paymentTransactionService.getPaymentsByScheduleId(1L)).thenReturn(Arrays.asList(tx1, tx2));

        mockMvc.perform(get("/api/v1/repayment-schedules/1/payment-transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].payment_reference").value("REF-001"))
                .andExpect(jsonPath("$.data[0].paid_amount").value(500000.00))
                .andExpect(jsonPath("$.data[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].payment_reference").value("REF-002"));

        verify(paymentTransactionService, times(1)).getPaymentsByScheduleId(1L);
    }
}