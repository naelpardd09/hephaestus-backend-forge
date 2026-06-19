package com.example.training.dto;

public class UserResponse {
    //untuk endpoint get/auth/me
    //jgn sampe ngereturn token/pw ya gais ya
    private String username;
    private String role;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
