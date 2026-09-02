package com.storvix.backend.service;

import com.storvix.backend.dto.AuthResponse;
import com.storvix.backend.dto.LoginRequest;
import com.storvix.backend.dto.RegisterRequest;
import com.storvix.backend.dto.UserResponse;
import com.storvix.backend.entity.User;
import com.storvix.backend.exception.AppException;
import com.storvix.backend.repository.UserRepository;
import com.storvix.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private AuthResponse buildTokens(User user) {
        String accessToken = jwtUtil.generateToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        user.setRefreshTokenHash(passwordEncoder.encode(refreshToken));
        userRepository.save(user);

        return AuthResponse.builder()
                .user(UserResponse.from(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        Optional<User> existing = userRepository.findByEmail(request.getEmail().toLowerCase());
        if (existing.isPresent()) {
            throw new AppException("Email already registered", HttpStatus.CONFLICT, "DUPLICATE_EMAIL");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider("LOCAL");
        userRepository.save(user);
        
        return buildTokens(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new AppException("Invalid email or password", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS"));
        
        if (!user.getIsActive()) {
            throw new AppException("Invalid email or password", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
        }
        if (!"LOCAL".equals(user.getProvider()) || user.getPassword() == null) {
            throw new AppException("Please use social login for this account", HttpStatus.BAD_REQUEST, "OAUTH_ACCOUNT");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException("Invalid email or password", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
        }

        return buildTokens(user);
    }

    public User getMe(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND, "NOT_FOUND"));
    }

    public void logout(String userId) {
        User user = getMe(userId);
        user.setRefreshTokenHash(null);
        userRepository.save(user);
    }

    public AuthResponse refreshTokens(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken) || !"refresh".equals(jwtUtil.extractType(refreshToken))) {
            throw new AppException("Invalid refresh token", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }
        String userId = jwtUtil.extractUserId(refreshToken);
        User user = getMe(userId);

        if (!user.getIsActive() || user.getRefreshTokenHash() == null) {
            throw new AppException("User not found or inactive", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }
        if (!passwordEncoder.matches(refreshToken, user.getRefreshTokenHash())) {
            throw new AppException("Refresh token revoked", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }
        return buildTokens(user);
    }
}
