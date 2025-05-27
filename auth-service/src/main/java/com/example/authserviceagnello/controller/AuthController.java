package com.example.authserviceagnello.controller;

import com.example.authserviceagnello.dto.APIResponse;
import com.example.authserviceagnello.dto.AuthResponse;
import com.example.authserviceagnello.dto.LoginRequest;
import com.example.authserviceagnello.dto.RegisterRequest;
import com.example.authserviceagnello.dto.TokenValidationResponse;
import com.example.authserviceagnello.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(APIResponse.success(response, "Usuário registrado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(APIResponse.success(response, "Login realizado com sucesso"));
    }

    @PostMapping("/validate")
    public ResponseEntity<APIResponse<TokenValidationResponse>> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(APIResponse.error("Token não fornecido ou formato inválido", 
                    org.springframework.http.HttpStatus.BAD_REQUEST));
        }
        
        String token = authHeader.substring(7);
        TokenValidationResponse response = authService.validateToken(token);
        return ResponseEntity.ok(APIResponse.success(response, "Token validado com sucesso"));
    }
} 