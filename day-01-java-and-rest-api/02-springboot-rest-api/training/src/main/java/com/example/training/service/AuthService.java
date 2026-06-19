package com.example.training.service;

import com.example.training.dto.LoginRequest;
import com.example.training.dto.LoginResponse;
import com.example.training.dto.UserResponse;
import com.example.training.model.User;
import com.example.training.security.AuthContext;
import com.example.training.security.AuthUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Method login: terima username+password, cek ke AuthContext.
    // Kalau cocok, kembalikan LoginResponse berisi token, username, role.
    // Kalau tidak cocok, return null (controller yang handle jadi 401).
    public LoginResponse login(LoginRequest request) {
        User user = AuthContext.getUserByCredentials(request.getUsername(), request.getPassword());

        if (user == null) {
            return null; // credentials salah
        }

        return new LoginResponse(user.getToken(), user.getUsername(), user.getRole());
    }

    // Method ini mengambil user yang sedang login berdasarkan token dari header.
    // Alurnya: baca header → extract token → cari user → return UserResponse
    // Kalau token tidak valid, return null (controller handle jadi 401).
    public UserResponse getCurrentUser(String authorizationHeader) {
        String token = AuthUtil.extractToken(authorizationHeader);
        User user = AuthContext.getUserByToken(token);
        
        if (user == null) {
            return null;
        }

        return new UserResponse(user.getUsername(), user.getRole());
    }
}