package com.fif.finance_training.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class CreatePaymentTransactionRequest {
    @NotNull(message = "repayment_schedule_id is required")
    @JsonProperty("repayment_schedule_id")
    private Long repaymentScheduleId;

    @NotNull(message = "payment_reference is required")
    @JsonProperty("payment_reference")
    private String paymentReference;

    @NotNull(message = "paid_amount is required")
    @Min(value = 1, message = "paid_amount must be greater than 0")
    @JsonProperty("paid_amount")
    private BigDecimal paidAmount;

    @NotNull(message = "paid_at is required")
    @JsonProperty("paid_at")
    private ZonedDateTime paidAt;
}