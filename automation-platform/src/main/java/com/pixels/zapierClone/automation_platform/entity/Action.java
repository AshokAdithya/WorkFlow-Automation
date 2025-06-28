package com.pixels.zapierClone.automation_platform.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "actions")
public class Action extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "action_definition_id")
    private ActionDefinition actionDefinition;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column( columnDefinition = "jsonb")
    private String inputConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionDefinition getActionDefinition() {
        return actionDefinition;
    }

    public void setActionDefinition(ActionDefinition actionDefinition) {
        this.actionDefinition = actionDefinition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInputConfig() {
        return inputConfig;
    }

    public void setInputConfig(String inputConfig) {
        this.inputConfig = inputConfig;
    }

    // Getters and setters
    // ...
}
