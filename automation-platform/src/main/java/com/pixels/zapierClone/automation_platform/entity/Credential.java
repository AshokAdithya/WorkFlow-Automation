package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "credentials")
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalAccountId;

    @ManyToOne
    @JoinColumn(name = "app_integration_id")
    private AppIntegration appIntegration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(String externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public AppIntegration getAppIntegration() {
        return appIntegration;
    }

    public void setAppIntegration(AppIntegration appIntegration) {
        this.appIntegration = appIntegration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getters and setters
    // ...
}
