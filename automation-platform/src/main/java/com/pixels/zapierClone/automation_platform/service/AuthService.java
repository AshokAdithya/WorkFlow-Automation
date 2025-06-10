package com.pixels.zapierClone.automation_platform.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pixels.zapierClone.automation_platform.entity.AuthProvider;
import com.pixels.zapierClone.automation_platform.entity.RefreshToken;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.repository.RefreshTokenRepository;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;
import com.pixels.zapierClone.automation_platform.security.JwtService;
import com.pixels.zapierClone.automation_platform.security.service.EmailService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import com.pixels.zapierClone.automation_platform.entity.Role;

@Service
@RequiredArgsConstructor
@Transactional
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

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRole(Role.USER);
        user.setProvider(AuthProvider.LOCAL);

        userRepository.save(user);

        sendVerificationEmail(user);
    }

    @Async
    public void sendVerificationEmail(User user) {
        String token = jwtService.generateVerificationToken(user.getEmail());
        String link = baseUrl + "/api/auth/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), link);
    }

    @Async
    public void verifyEmail(String token) {
        if (!jwtService.validateVerificationToken(token)) {
            throw new RuntimeException("Invalid or expired verification token");
        }
        String email = jwtService.extractEmailFromVerificationToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerified(true);
        userRepository.save(user);
    }

    public void signin(HttpServletResponse response,String email, String password) {
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

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshTokenStr = jwtService.generateRefreshToken();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenStr);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(refreshToken);

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true); // use only if HTTPS
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 15); // 15 mins

        Cookie refreshCookie = new Cookie("refreshToken", refreshTokenStr);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    public void logout(HttpServletRequest request,HttpServletResponse response){
        Cookie refreshTokenOld = getCookie(request,"refreshToken");
        if(refreshTokenOld!=null){
            refreshTokenRepository.deleteByToken(refreshTokenOld.getValue());
        }

        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/"); // Same path as used when setting it
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0); // Delete it
        accessTokenCookie.setSecure(false); // If your app uses HTTPS


        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setSecure(false);

        // Add them to response
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    @Scheduled(fixedRate = 86400000) 
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredSince(Instant.now());
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

}
