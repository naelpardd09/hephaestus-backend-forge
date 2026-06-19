package com.example.training.controller;

import com.example.training.dto.LoginRequest;
import com.example.training.dto.LoginResponse;
import com.example.training.dto.UserResponse;
import com.example.training.dto.ErrorResponse;
import com.example.training.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth") //base URL utk smua endpoint di class
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    //@RestController itu ngastau Spring kalo class ini tuh controllernya
    //dan tiap method otomatis return JSON

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        //ResponseEntity wrapper dari Spring isinya http response (inc status code)
        //? artinya body nya bs apa aja, bisa return LoginResponse, bs jg ErrorResponse
        LoginResponse response = authService.login(request);

        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Invalid username or password", null));
        }

        return ResponseEntity.ok(response);
    }
    //nah PostMapping ini fungsinya mappingin/metain method ini ke si url API td.
    //Request Body untuk ngasitau Spring untuk ngubah JSON dari req body ke objek LoginRequest
    //ntr hasilnya dikirim ke authService.login(), kalo USERNAME/PW SALAH, return null, kalo berhasil
    //si ResponseEntity.ok() ini ke-execute(status 200 dkk)

    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        UserResponse response = authService.getCurrentUser(authorizationHeader);

        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error("UNAUTHORIZED", "Authentication is required", null));
        }

        return ResponseEntity.ok(response);
    }
    //@RequestHeader ini fungsinya membaca header Authorization(value) dari HTPP Request
    //dan masukinnya ke parameter "authorizationHeader.required=false",
    //jadi misal header ga di send, spring ga lgsg throw error, jd kita yg handle dengan return 401 itu td.
    //Header lalu diteruskan ke service , service yg urus extract token dan cari usernya

}