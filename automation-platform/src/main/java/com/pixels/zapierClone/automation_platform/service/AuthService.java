package com.pixels.zapierClone.automation_platform.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pixels.zapierClone.automation_platform.entity.AuthProvider;
import com.pixels.zapierClone.automation_platform.entity.RefreshToken;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.repository.RefreshTokenRepository;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;
import com.pixels.zapierClone.automation_platform.security.JwtService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import com.pixels.zapierClone.automation_platform.entity.Role;
import com.pixels.zapierClone.automation_platform.security.security.EmailService;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public void signup(String email, String password, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .userRole(Role.USER)
                .provider(AuthProvider.LOCAL)
                .build();

        userRepository.save(user);

        sendVerificationEmail(user);
    }

    public void sendVerificationEmail(User user) {
        String token = jwtService.generateVerificationToken(user.getEmail());
        String link = baseUrl + "/api/auth/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), link);
    }

    public void verifyEmail(String token) {
        if (!jwtService.validateVerificationToken(token)) {
            throw new RuntimeException("Invalid or expired verification token");
        }
        String email = jwtService.extractEmailFromVerificationToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerified(true);
        userRepository.save(user);
    }

    public Map<String, String> signin(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new RuntimeException("Use your social login to sign in");
        }

        if (!user.isVerified()) {
            sendVerificationEmail(user);  // optionally resend verification email
            throw new RuntimeException("Email not verified. Please verify your email.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return generateTokens(user);
    }

    private Map<String, String> generateTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshTokenStr = jwtService.generateRefreshToken();

        // Delete old refresh tokens for user
        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenRepository.save(refreshToken);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshTokenStr
        );
    }
}
