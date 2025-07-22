package com.sachin.Login.service;

import java.util.Optional;
import java.util.UUID;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.sachin.Login.domain.entities.Token;
import com.sachin.Login.domain.entities.UserModel;
import com.sachin.Login.repository.TokenRepository;
import com.sachin.Login.repository.UserRepository;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository, JWTService jwtService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;

    }

    public Token createRefreshToken(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User is not found or Incorrect Email"));
        Token refreshToken = new Token();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtService.generateRefreshToken(email));
        refreshToken.setExpire(Instant.now().plus(Duration.ofDays(1)));
        return tokenRepository.save(refreshToken);
    }

    public Token verifyExpiryToken(Token token) {
        if (token.getExpire().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token was expired. Please login again");
        }
        return token;
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    // public void revokeAllUserTokens(UserModel user) {
    // var validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
    // validTokens.forEach(token -> {
    // token.setExpire(Instant.now().minus(Duration.ofDays(1)));
    // token.setRevoked(true);
    // });
    // tokenRepository.saveAll(validTokens);
    // }

    public Optional<String> getValidRefreshToken(UUID userId) {
        return tokenRepository
                .findByUserId(userId)
                .stream()
                .filter(token -> !token.isRevoked())
                .filter(token -> token.getExpire().isAfter(Instant.now()))
                .findFirst()
                .map(Token::getToken);
    }

}
