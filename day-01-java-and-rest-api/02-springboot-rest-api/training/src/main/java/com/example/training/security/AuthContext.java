package com.example.training.security;

import com.example.training.model.User;
import java.util.HashMap;
import java.util.Map;
//kelas ini fungsinya utk database dummy user, terkait uname dan pw credentials
public class AuthContext {

    // Map ini menyimpan semua dummy user, dengan token sebagai key.
    // Kenapa token sebagai key? Supaya pencarian user berdasarkan token O(1) / cepat.
    private static final Map<String, User> TOKEN_MAP = new HashMap<>();

    // Static block ini dijalankan sekali saat class pertama kali di-load.
    // Di sinilah kita daftarkan semua dummy user sesuai tabel di exercise.
    static {
        TOKEN_MAP.put("token-admin",    new User("joker",    "joker123",    "ADMIN",    "token-admin"));
        TOKEN_MAP.put("token-staff",    new User("jack",    "jack123",    "STAFF",    "token-staff"));
        TOKEN_MAP.put("token-approver", new User("queen", "queen123", "APPROVER", "token-approver"));
        TOKEN_MAP.put("token-manager" , new User("king", "king123", "MANAGER", "token-manager"));
    }
    //TOKEN_MAP ini di-declare private static final krn datanya final dan private, spy ga terakses di map

    // Method ini mencari User berdasarkan token.
    // Return null kalau token tidak ditemukan (artinya token tidak valid).
    public static User getUserByToken(String token) {
        if (token == null) return null;
        return TOKEN_MAP.get(token);
    }

    // Method ini mencari User berdasarkan username + password.
    // Dipakai saat login. Loop semua user, cocokkan username & password.
    public static User getUserByCredentials(String username, String password) {
        for (User user : TOKEN_MAP.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // tidak ditemukan = credentials salah
    }
}