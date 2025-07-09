package com.sachin.Login.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sachin.Login.domain.dtos.UserDto;
import com.sachin.Login.domain.entities.UserModel;
import com.sachin.Login.mapper.UserDtoMapper;
import com.sachin.Login.repository.UserRepository;
import com.sachin.Login.service.JWTService;
import com.sachin.Login.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserDtoMapper mapper;

    public AuthServiceImpl(UserRepository repo, AuthenticationManager authenticationManager, JWTService jwtService,
            UserDtoMapper mapper) {
        this.repo = repo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mapper = mapper;

    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public UserModel register(UserModel user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public String verify(UserModel user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {

            String token = jwtService.generateToken(user.getEmail());
            return token;
        }
        return null;
    }

    @Override
    public UserDto getMe(String email) {
        UserModel user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return mapper.apply(user);
    }

}
