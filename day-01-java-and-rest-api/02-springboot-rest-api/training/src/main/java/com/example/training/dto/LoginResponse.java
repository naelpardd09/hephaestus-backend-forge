package com.example.training.dto;

import lombok.Data;

@Data
public class LoginResponse {
    //ini berisi nantinya respon drpd login
    //yaitu keluarannya token, username, dan role
    private String token;
    private String username;
    private String role;
    
    public LoginResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
