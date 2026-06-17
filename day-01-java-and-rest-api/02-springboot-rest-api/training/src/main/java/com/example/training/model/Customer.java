package com.example.training.model;

import java.time.ZonedDateTime;

public class Customer {
    // nah disini kita buat basis Customer ini bakal punya apa aja var nya. 
    // misalnya dia punya id, nama lengkap, notelp, dan email. nah nanti kita buat constructor, getter dan setter nya juga.
    // kenapa dia private?
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // ini constructor kosong (dia itu kayak method tapi tanpa parameter tanpa return), 
    

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

    public Customer() {
    }

    public Customer(Long id, String fullName, String email, String phoneNumber, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

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
