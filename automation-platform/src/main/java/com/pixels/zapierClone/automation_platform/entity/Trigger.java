// // 
// package com.pixels.zapierClone.automation_platform.entity;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import jakarta.persistence.*;

// @Entity
// @Table(name = "triggers")
// public class Trigger {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @JsonBackReference
//     @OneToOne
//     @JoinColumn(name = "workflow_id", unique = true)
//     private Workflow workflow;

//     @ManyToOne
//     @JoinColumn(name = "app_integration_id")
//     private AppIntegration appIntegration;

//     private String triggerTypeIdentifier;
//     private String name;
//     private String description;

//     @Column(columnDefinition = "jsonb")
//     private String configJson;

//     @Column(unique = true)
//     private String webhookPath;

//     // Constructors
//     public Trigger() {
//     }

//     public Trigger(Long id) {
//         this.id = id;
//     }

//     // Getters and Setters

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public Workflow getWorkflow() {
//         return workflow;
//     }

//     public void setWorkflow(Workflow workflow) {
//         this.workflow = workflow;
//     }

//     public AppIntegration getAppIntegration() {
//         return appIntegration;
//     }

//     public void setAppIntegration(AppIntegration appIntegration) {
//         this.appIntegration = appIntegration;
//     }

//     public String getTriggerTypeIdentifier() {
//         return triggerTypeIdentifier;
//     }

//     public void setTriggerTypeIdentifier(String triggerTypeIdentifier) {
//         this.triggerTypeIdentifier = triggerTypeIdentifier;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public String getDescription() {
//         return description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public String getConfigJson() {
//         return configJson;
//     }

//     public void setConfigJson(String configJson) {
//         this.configJson = configJson;
//     }

//     public String getWebhookPath() { return webhookPath; }
//     public void setWebhookPath(String webhookPath) { this.webhookPath = webhookPath; }
// }


// package com.pixels.zapierClone.automation_platform.entity;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import jakarta.persistence.*;
// import org.hibernate.annotations.JdbcTypeCode; // Added import
// import org.hibernate.type.SqlTypes; // Added import

// @Entity
// @Table(name="triggers")
// public class Trigger {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @JsonBackReference
//     @OneToOne
//     @JoinColumn(name = "workflow_id", unique = true)
//     private Workflow workflow;

//     @ManyToOne
//     @JoinColumn(name = "app_integration_id")
//     private AppIntegration appIntegration;

//     private String triggerTypeIdentifier;
//     private String name;
//     private String description;

//     @JdbcTypeCode(SqlTypes.JSON) // Added this line
//     @Column(columnDefinition = "jsonb")
//     private String configJson;

//     @Column(unique = true)
//     private String webhookPath;

//     public Trigger() {}

//     public Trigger(Long id) {
//         this.id = id;
//     }

//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
//     public Workflow getWorkflow() { return workflow; }
//     public void setWorkflow(Workflow workflow) { this.workflow = workflow; }
//     public AppIntegration getAppIntegration() { return appIntegration; }
//     public void setAppIntegration(AppIntegration appIntegration) { this.appIntegration = appIntegration; }
//     public String getTriggerTypeIdentifier() { return triggerTypeIdentifier; }
//     public void setTriggerTypeIdentifier(String triggerTypeIdentifier) { this.triggerTypeIdentifier = triggerTypeIdentifier; }
//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }
//     public String getDescription() { return description; }
//     public void setDescription(String description) { this.description = description; }
//     public String getConfigJson() { return configJson; }
//     public void setConfigJson(String configJson) { this.configJson = configJson; }
//     public String getWebhookPath() { return webhookPath; }
//     public void setWebhookPath(String webhookPath) { this.webhookPath = webhookPath; }
// }

package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "triggers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Trigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "workflow_id", unique = true)
    private Workflow workflow;

    @ManyToOne
    @JoinColumn(name = "app_integration_id")
    private AppIntegration appIntegration;

    private String triggerTypeIdentifier;
    private String name;
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String configJson;

    @Column(unique = true)
    private String webhookPath;

    public Trigger() {}

    public Trigger(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Workflow getWorkflow() { return workflow; }
    public void setWorkflow(Workflow workflow) { this.workflow = workflow; }
    public AppIntegration getAppIntegration() { return appIntegration; }
    public void setAppIntegration(AppIntegration appIntegration) { this.appIntegration = appIntegration; }
    public String getTriggerTypeIdentifier() { return triggerTypeIdentifier; }
    public void setTriggerTypeIdentifier(String triggerTypeIdentifier) { this.triggerTypeIdentifier = triggerTypeIdentifier; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getConfigJson() { return configJson; }
    public void setConfigJson(String configJson) { this.configJson = configJson; }
    public String getWebhookPath() { return webhookPath; }
    public void setWebhookPath(String webhookPath) { this.webhookPath = webhookPath; }
}
