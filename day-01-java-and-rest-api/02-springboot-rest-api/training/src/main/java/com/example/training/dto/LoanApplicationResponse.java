package com.example.training.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;
@Data
public class LoanApplicationResponse {

    private Long id;

    @JsonProperty("customer_id")
    private Long customerId;

    @JsonProperty("loan_amount")
    private BigDecimal loanAmount;

    @JsonProperty("tenor_month")
    private int tenorMonth;

    private String purpose;

    // Status bisa: SUBMITTED, APPROVED, REJECTED
    private String status;

    public LoanApplicationResponse() {}

    public LoanApplicationResponse(Long id, Long customerId, BigDecimal loanAmount, int tenorMonth, String purpose, String status) {
        this.id = id;
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.tenorMonth = tenorMonth;
        this.purpose = purpose;
        this.status = status;
    }

}