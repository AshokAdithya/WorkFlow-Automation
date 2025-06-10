// package com.pixels.zapierClone.automation_platform.entity;

// import java.time.Instant;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Data
// @Table(name="app_integrations")
// @AllArgsConstructor
// @NoArgsConstructor
// public class AppIntegration {
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name; // e.g. "Slack", "Gmail"
//     private String identifier; // "slack", "gmail"
//     private boolean oauthRequired;
// }

// package com.pixels.zapierClone.automation_platform.entity;

// import com.fasterxml.jackson.annotation.JsonManagedReference;
// import jakarta.persistence.*;

// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Table(name = "app_integration")
// public class AppIntegration {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;
//     private String identifier;
//     private String description;
//     private String logoUrl;
//     private String authType;

//     @JsonManagedReference
//     @OneToMany(
//         mappedBy = "appIntegration",
//         cascade = CascadeType.ALL,
//         orphanRemoval = true,
//         fetch = FetchType.LAZY
//     )
//     private List<Trigger> triggers = new ArrayList<>();

//     @JsonManagedReference
//     @OneToMany(
//         mappedBy = "appIntegration",
//         cascade = CascadeType.ALL,
//         orphanRemoval = true,
//         fetch = FetchType.LAZY
//     )
//     private List<Action> actions = new ArrayList<>();

//     // Getters and Setters

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public String getIdentifier() {
//         return identifier;
//     }

//     public void setIdentifier(String identifier) {
//         this.identifier = identifier;
//     }

//     public String getDescription() {
//         return description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public String getLogoUrl() {
//         return logoUrl;
//     }

//     public void setLogoUrl(String logoUrl) {
//         this.logoUrl = logoUrl;
//     }

//     public String getAuthType() {
//         return authType;
//     }

//     public void setAuthType(String authType) {
//         this.authType = authType;
//     }

//     public List<Trigger> getTriggers() {
//         return triggers;
//     }

//     public void setTriggers(List<Trigger> triggers) {
//         this.triggers.clear();
//         if (triggers != null) {
//             this.triggers.addAll(triggers);
//         }
//     }

//     public List<Action> getActions() {
//         return actions;
//     }

//     public void setActions(List<Action> actions) {
//         this.actions.clear();
//         if (actions != null) {
//             this.actions.addAll(actions);
//         }
//     }
// }


package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "app_integration")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String identifier;
    private String description;
    private String logoUrl;
    private String authType;

    @OneToMany(mappedBy = "appIntegration", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Trigger> triggers = new ArrayList<>();

    @OneToMany(mappedBy = "appIntegration", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Action> actions = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }

    public List<Trigger> getTriggers() { return triggers; }
    public void setTriggers(List<Trigger> triggers) {
        this.triggers.clear();
        if (triggers != null) {
            this.triggers.addAll(triggers);
        }
    }

    public List<Action> getActions() { return actions; }
    public void setActions(List<Action> actions) {
        this.actions.clear();
        if (actions != null) {
            this.actions.addAll(actions);
        }
    }
}
