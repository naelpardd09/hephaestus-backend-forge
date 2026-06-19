package com.example.training.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CreateLoanApplicationRequest {

    // @NotNull supaya customer_id wajib dikirim
    @JsonProperty("customer_id")
    private Long customerId;

    //@Positive supaya loan amount tidak negatif
    @NotNull
    @Positive
    @JsonProperty("loan_amount")
    private BigDecimal loanAmount;

    //@Min(1) supaya tenor minimal 1 bulan
    @NotNull
    @Min(1)
    @JsonProperty("tenor_month")
    private Integer tenorMonth;

    //@NotBlank spy purpose tidak kosong
    @NotBlank
    private String purpose;
}

    