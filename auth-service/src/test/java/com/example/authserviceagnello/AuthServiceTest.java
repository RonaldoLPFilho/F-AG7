package com.example.authserviceagnello;

import com.example.authserviceagnello.dto.AuthResponse;
import com.example.authserviceagnello.dto.LoginRequest;
import com.example.authserviceagnello.dto.RegisterRequest;
import com.example.authserviceagnello.exception.UserAlreadyExistsException;
import com.example.authserviceagnello.exception.UserNotFoundException;
import com.example.authserviceagnello.model.User;
import com.example.authserviceagnello.model.UserRole;
import com.example.authserviceagnello.repository.UserRepository;
import com.example.authserviceagnello.security.JwtService;
import com.example.authserviceagnello.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Testes de Registro")
    class RegisterTests {
        private RegisterRequest registerRequest;

        @BeforeEach
        void setUp() {
            registerRequest = new RegisterRequest();
            registerRequest.setUsername("usuario");
            registerRequest.setEmail("usuario@email.com");
            registerRequest.setPassword("senha");
            registerRequest.setFullName("Nome Completo");
            registerRequest.setRole(UserRole.USER);
        }

        @Test
        @DisplayName("Deve registrar usuário com sucesso")
        void register_DeveRegistrarUsuarioComSucesso() {
            AuthResponse response = authService.register(registerRequest);

            assertNotNull(response);
            assertNotNull(response.getToken());
            assertEquals("usuario", response.getUsername());
            assertEquals("USER", response.getRole());

            Optional<User> savedUser = userRepository.findByUsername("usuario");
            assertTrue(savedUser.isPresent());
            assertEquals("usuario@email.com", savedUser.get().getEmail());
            assertEquals("Nome Completo", savedUser.get().getFullName());
        }

        @Test
        @DisplayName("Deve lançar exceção quando username já existe")
        void register_DeveLancarExcecaoQuandoUsernameJaExiste() {
            authService.register(registerRequest);

            assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequest));
        }

        @Test
        @DisplayName("Deve lançar exceção quando email já existe")
        void register_DeveLancarExcecaoQuandoEmailJaExiste() {
            authService.register(registerRequest);

            RegisterRequest newRequest = new RegisterRequest();
            newRequest.setUsername("outro_usuario");
            newRequest.setEmail("usuario@email.com");
            newRequest.setPassword("senha");
            newRequest.setFullName("Outro Nome");
            newRequest.setRole(UserRole.USER);

            assertThrows(UserAlreadyExistsException.class, () -> authService.register(newRequest));
        }
    }

    @Nested
    @DisplayName("Testes de Login")
    class LoginTests {
        private LoginRequest loginRequest;
        private User user;

        @BeforeEach
        void setUp() {
            user = User.builder()
                    .username("usuario")
                    .email("usuario@email.com")
                    .password(passwordEncoder.encode("senha"))
                    .fullName("Nome Completo")
                    .role(UserRole.USER)
                    .isActive(true)
                    .build();
            userRepository.save(user);

            loginRequest = new LoginRequest();
            loginRequest.setUsername("usuario");
            loginRequest.setPassword("senha");
        }

        @Test
        @DisplayName("Deve realizar login com sucesso")
        void login_DeveRealizarLoginComSucesso() {
            AuthResponse response = authService.login(loginRequest);

            assertNotNull(response);
            assertNotNull(response.getToken());
            assertEquals("usuario", response.getUsername());
            assertEquals("USER", response.getRole());
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não encontrado")
        void login_DeveLancarExcecaoQuandoUsuarioNaoEncontrado() {
            loginRequest.setUsername("usuario_inexistente");
            assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha está incorreta")
        void login_DeveLancarExcecaoQuandoSenhaIncorreta() {
            loginRequest.setPassword("senha_incorreta");
            assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        }
    }
} 