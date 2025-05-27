package com.example.authserviceagnello.dto;

import com.example.authserviceagnello.model.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private UserRole role = UserRole.USER; // Valor padrão é USER
} 