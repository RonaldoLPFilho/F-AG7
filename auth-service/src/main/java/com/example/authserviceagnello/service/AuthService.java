package com.example.authserviceagnello.service;

import com.example.authserviceagnello.dto.AuthResponse;
import com.example.authserviceagnello.dto.LoginRequest;
import com.example.authserviceagnello.dto.RegisterRequest;
import com.example.authserviceagnello.dto.TokenValidationResponse;
import com.example.authserviceagnello.exception.InvalidTokenException;
import com.example.authserviceagnello.exception.UserAlreadyExistsException;
import com.example.authserviceagnello.exception.UserNotFoundException;
import com.example.authserviceagnello.model.User;
import com.example.authserviceagnello.repository.UserRepository;
import com.example.authserviceagnello.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username já existe");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email já existe");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole())
                .isActive(true)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, user.getUsername(), user.getRole().name());
    }

    public TokenValidationResponse validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

            if (jwtService.isTokenValid(token, userDetails)) {
                return new TokenValidationResponse(true, "Token válido");
            } else {
                throw new InvalidTokenException("Token inválido");
            }
        } catch (Exception e) {
            throw new InvalidTokenException("Token inválido: " + e.getMessage());
        }
    }
} 