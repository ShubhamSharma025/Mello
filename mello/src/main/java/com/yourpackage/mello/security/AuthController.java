package com.yourpackage.mello.security;

import com.yourpackage.mello.model.User;

import java.util.Map;
import com.yourpackage.mello.security.LoginRequest;
import com.yourpackage.mello.security.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // ✅ consistent with frontend URL
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🔑 Login → return JWT
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
       return ResponseEntity.ok(authService.login(request));
    }

    // DTOQ
    // public record LoginRequest(String email, String password) {}

    // 🆕 Signup → create user & return JWT
    @PostMapping("/signup")
      public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest singupRequest) {
       return ResponseEntity.ok(authService.signup(singupRequest));
    }

    // // ✅ Public test endpoint
    // @GetMapping("/hello")
    // public String hello() {
    //     return "Hello! No token needed here.";
    // }
}
