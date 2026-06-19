package com.example.training.security;

public class AuthUtil {
    //fungsinya untuk extract token dari header Authorization

    // Method ini membaca nilai header Authorization, lalu mengambil token-nya.
    // Format header yang diharapkan: "Bearer token-admin"
    // Kalau header null atau tidak diawali "Bearer ", return null
    public static String extractToken(String authorizationHeader) {

        // Kalau header tidak ada sama sekali
        if (authorizationHeader == null) {
            return null; //return 401
        }

        // Kalau tidak diawali "Bearer ", format salah
        if (!authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        // Ambil substring setelah "Bearer " (7 karakter)
        // Contoh: "Bearer token-admin" → "token-admin"
        return authorizationHeader.substring(7);
    }
}