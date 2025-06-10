// package com.pixels.zapierClone.automation_platform.entity;

// import java.time.Instant;

// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Data
// @Table(name = "workflows")
// @AllArgsConstructor
// @NoArgsConstructor
// public class Workflow {
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "user_id", nullable = false)
//     private User user;

//     private boolean enabled;

//     private Instant createdAt;
//     private Instant updatedAt;
// }

// package com.pixels.zapierClone.automation_platform.entity;

// import com.fasterxml.jackson.annotation.JsonManagedReference;
// import jakarta.persistence.*;

// import java.time.Instant;
// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Table(name = "workflows")
// public class Workflow {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;
//     private boolean isEnabled;
//     private Instant createdAt;
//     private Instant updatedAt;

//     @ManyToOne
//     @JoinColumn(name = "user_id")
//     private User user;

//     @JsonManagedReference
//     @OneToOne(
//         mappedBy = "workflow",
//         cascade = CascadeType.ALL,
//         orphanRemoval = true,
//         fetch = FetchType.LAZY
//     )
//     private Trigger trigger;

//     @JsonManagedReference
//     @OneToMany(
//         mappedBy = "workflow",
//         cascade = CascadeType.ALL,
//         orphanRemoval = true,
//         fetch = FetchType.LAZY
//     )
//     @OrderBy("stepOrder ASC")
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

//     public boolean isEnabled() {
//         return isEnabled;
//     }

//     public void setEnabled(boolean enabled) {
//         isEnabled = enabled;
//     }

//     public Instant getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(Instant createdAt) {
//         this.createdAt = createdAt;
//     }

//     public Instant getUpdatedAt() {
//         return updatedAt;
//     }

//     public void setUpdatedAt(Instant updatedAt) {
//         this.updatedAt = updatedAt;
//     }

//     public User getUser() {
//         return user;
//     }

//     public void setUser(User user) {
//         this.user = user;
//     }

//     public Trigger getTrigger() {
//         return trigger;
//     }

//     public void setTrigger(Trigger trigger) {
//         this.trigger = trigger;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflows")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isEnabled;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(
        mappedBy = "workflow",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Trigger trigger;

    @OneToMany(
        mappedBy = "workflow",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @OrderBy("stepOrder ASC")
    private List<Action> actions = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { isEnabled = enabled; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Trigger getTrigger() { return trigger; }
    public void setTrigger(Trigger trigger) { this.trigger = trigger; }
    public List<Action> getActions() { return actions; }
    public void setActions(List<Action> actions) {
        this.actions.clear();
        if (actions != null) {
            this.actions.addAll(actions);
        }
    }
}
