package com.example.training.security;

import java.util.Arrays;
import java.util.List;

public class RoleValidator {

    // Method ini mengecek apakah role tertentu ada di dalam daftar role yang diizinkan.
    // Contoh penggunaan: hasRole("STAFF", "ADMIN", "STAFF") → true
    //                    hasRole("APPROVER", "ADMIN", "STAFF") → false
    //
    // Parameter allowedRoles menggunakan varargs (...) supaya bisa ditulis fleksibel:
    //   hasRole(role, "ADMIN", "STAFF", "APPROVER")
    public static boolean hasRole(String userRole, String... allowedRoles) {
        List<String> allowed = Arrays.asList(allowedRoles);
        return allowed.contains(userRole);
    }
}