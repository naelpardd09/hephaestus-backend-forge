package com.example.training.model;
import lombok.Data;

@Data
public class User {
    //ini untuk nge-declare kalo kelas User bakal ada
    //username, password, role, dan token
    
    private String username;
    private String password;
    private String role;
    private String token;
    
    public User(String username, String password, String role, String token) { //ini constructor
        this.username = username;
        this.password = password;
        this.role = role;
        this.token = token;
    }

    //Getter and Setters automatic via lombok ya gais.


}
