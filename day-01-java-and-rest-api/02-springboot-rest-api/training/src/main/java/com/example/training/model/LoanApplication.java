package com.example.training.model;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class LoanApplication {
    private Long id;
    private Long customerId;
    private BigDecimal loanAmount;
    private int tenorMonth;
    private String purpose;
    private String status; //ini tuh status nanti gmn

    public LoanApplication(){}

    public LoanApplication(Long id, Long customerId, BigDecimal loanAmount, int tenorMonth, String purpose,
            String status) {
        this.id = id;
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.tenorMonth = tenorMonth;
        this.purpose = purpose;
        this.status = status;
    }

    

}
    
    