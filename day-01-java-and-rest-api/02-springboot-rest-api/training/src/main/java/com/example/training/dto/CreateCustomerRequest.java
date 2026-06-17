package com.example.training.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints

// ini adalah constructor yang akan digunakan untuk menerima data 
// dari client ketika ingin membuat customer baru, 
// jadi nanti di controller kita akan menerima object ini 
// sebagai parameter untuk create customer.
public class CreateCustomerRequest {
    // disini kita declare field apa aja yg dibutuhin
    // untuk buat customer baru.
    // nah disini cthnya kita butuh nama lengkap, email, dan nomor telepon 
    // untuk buat customer baru.
    @NotBlank
    @Size(min =  3, max = 100)

    @JsonProperty("full_name")
    private String fullName;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Min(10)
    @JsonProperty("phone_number")
    private String phoneNumber;

    public CreateCustomerRequest(@NotBlank @Size(min = 3, max = 100) String fullName, @NotNull @Email String email,
            @NotNull @Min(10) String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    // nah ini getter and setter 
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    
}
