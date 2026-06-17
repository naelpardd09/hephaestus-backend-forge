package com.example.training.dto;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;



public class PatchCustomerRequest {
    @JsonProperty("full_name")
    private String fullName;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public PatchCustomerRequest(String fullName, String email, String phoneNumber, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}
