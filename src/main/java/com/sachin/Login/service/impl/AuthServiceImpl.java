package com.sachin.Login.service.impl;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sachin.Login.domain.dtos.AuthResponse;
import com.sachin.Login.domain.dtos.UserDto;
import com.sachin.Login.domain.entities.UserModel;
import com.sachin.Login.mapper.UserDtoMapper;
import com.sachin.Login.repository.UserRepository;
import com.sachin.Login.service.JWTService;
import com.sachin.Login.service.TokenService;
import com.sachin.Login.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserDtoMapper mapper;
    private final TokenService tokenService;

    public AuthServiceImpl(UserRepository repo, AuthenticationManager authenticationManager, JWTService jwtService,
            UserDtoMapper mapper, TokenService tokenService) {
        this.repo = repo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mapper = mapper;
        this.tokenService = tokenService;

    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public UserModel register(UserModel user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public AuthResponse verify(UserModel user) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        user.getEmail(), user.getPassword()));

        if (!authentication.isAuthenticated()) {

            throw new BadCredentialsException("Invalid email or password");
        }
        String accessToken = jwtService.generateToken(user.getEmail());
        UUID id = repo.findByEmail(user.getEmail()).get().getId();
        String refreshToken = tokenService
                .getValidRefreshToken(id)
                .orElseGet(() -> tokenService.createRefreshToken(user.getEmail())
                        .getToken());
        System.out.printf("refreshToken: \n", refreshToken);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public UserDto getMe(String email) {
        UserModel user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return mapper.apply(user);
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        var token = tokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        tokenService.verifyExpiryToken(token);
        String newAccessToken = jwtService.generateRefreshToken(refreshToken);
        return new AuthResponse(newAccessToken, refreshToken);
    }

}