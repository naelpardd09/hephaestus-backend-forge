package com.example.training.dto;
import java.time.ZonedDateTime;
import org.springframework.cglib.core.Local;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class CustomerResponse {
    //disini buat fields apa aja sih yang sekiranya bakal 
    // dikirim ke client setelah berhasil create customer, 
    // misalnya id, fullName, email, phoneNumber.
    @JsonProperty
    private Long id;
    private String fullName;
    private String email;
    @JsonProperty
    private String phoneNumber;
    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("updated_at")
    private ZonedDateTime updatedAt;




    public CustomerResponse(){}

    public CustomerResponse(Long id, String fullName, String email, String phoneNumber, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    //Getter and Setters
}
