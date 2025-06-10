package com.pixels.zapierClone.automation_platform.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import com.pixels.zapierClone.automation_platform.dto.RefreshTokenRequest;
import com.pixels.zapierClone.automation_platform.dto.SigninRequest;
import com.pixels.zapierClone.automation_platform.dto.SignupRequest;
import com.pixels.zapierClone.automation_platform.entity.AuthProvider;
import com.pixels.zapierClone.automation_platform.entity.RefreshToken;
import com.pixels.zapierClone.automation_platform.entity.Role;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.repository.RefreshTokenRepository;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;
import com.pixels.zapierClone.automation_platform.security.JwtService;
import com.pixels.zapierClone.automation_platform.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request.getEmail(), request.getPassword(), request.getName());
        return ResponseEntity.ok("User registered successfully.\n Please verify your email");

    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest request, HttpServletResponse response) {
        authService.signin(response,request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Login Successfull");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response){
        authService.logout(request,response);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie accessCookie = getCookie(request, "accessToken");
        if (accessCookie != null) {
            String accessToken = accessCookie.getValue();
            if (!jwtService.isTokenExpired(accessToken)) {
                return ResponseEntity.ok("Access token still valid");
            }
        }

        Cookie refreshCookie = getCookie(request, "refreshToken");
        if (refreshCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token");
        }

        String refreshToken = refreshCookie.getValue();
        var tokenOpt = refreshTokenRepository.findByToken(refreshToken);
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpiryDate().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token invalid or expired");
        }

        String email = tokenOpt.get().getUser().getEmail();
        String newAccessToken = jwtService.generateAccessToken(email);

        Cookie newAccessCookie = new Cookie("accessToken", newAccessToken);
        newAccessCookie.setHttpOnly(true);
        newAccessCookie.setSecure(false); // Consider setting this to true in production (HTTPS)
        newAccessCookie.setPath("/");
        newAccessCookie.setMaxAge(60 * 15); // 15 minutes

        response.addCookie(newAccessCookie);

        return ResponseEntity.ok("Access token refreshed");
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
