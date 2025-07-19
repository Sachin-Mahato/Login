package com.sachin.Login.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.Login.domain.dtos.AuthResponse;
import com.sachin.Login.domain.dtos.UserDto;
import com.sachin.Login.domain.entities.UserModel;
import com.sachin.Login.service.AuthService;

@RestController
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user) {
        UserModel savedUser = service.register(user);

        if (savedUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User is created successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> verify(@RequestBody UserModel user) {
        AuthResponse tokens = service.verify(user);
        if (tokens == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credential");
        }

        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserDetails(Authentication authentication) {
        String email = authentication.getName();
        UserDto name = service.getMe(email);
        return ResponseEntity.ok(name);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        AuthResponse tokens = service.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(tokens);

    }
}
