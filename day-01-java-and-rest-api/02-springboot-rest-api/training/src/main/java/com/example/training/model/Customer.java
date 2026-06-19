package com.example.training.model;

import java.time.ZonedDateTime;

import lombok.Data;
@Data
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
    public Customer() {
    }

    public Customer(Long id, String fullName, String email, String phoneNumber, ZonedDateTime createdAt, ZonedDateTime updatedAt) { //ini constructor
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}