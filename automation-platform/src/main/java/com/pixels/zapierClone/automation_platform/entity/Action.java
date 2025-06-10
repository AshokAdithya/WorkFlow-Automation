// package com.pixels.zapierClone.automation_platform.entity;

// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.Lob;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Data
// @Table(name = "actions")
// @AllArgsConstructor
// @NoArgsConstructor
// public class Action {
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String type;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "workflow_id", nullable = false)
//     private Workflow workflow;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "app_integration_id", nullable = false)
//     private AppIntegration appIntegration;

//     @Lob
//     private String configJson;

//     private Integer stepOrder;
// }

// package com.pixels.zapierClone.automation_platform.entity;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import jakarta.persistence.*;

// @Entity
// @Table(name = "actions")
// public class Action {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @JsonBackReference
//     @ManyToOne
//     @JoinColumn(name = "workflow_id")
//     private Workflow workflow;

//     @ManyToOne
//     @JoinColumn(name = "app_integration_id")
//     private AppIntegration appIntegration;

//     private String actionTypeIdentifier;
//     private String name;
//     private String description;
//     private Integer stepOrder;

//     @Column(columnDefinition = "jsonb")
//     private String configJson;

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

//     public String getActionTypeIdentifier() {
//         return actionTypeIdentifier;
//     }

//     public void setActionTypeIdentifier(String actionTypeIdentifier) {
//         this.actionTypeIdentifier = actionTypeIdentifier;
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

//     public Integer getStepOrder() {
//         return stepOrder;
//     }

//     public void setStepOrder(Integer stepOrder) {
//         this.stepOrder = stepOrder;
//     }

//     public String getConfigJson() {
//         return configJson;
//     }

//     public void setConfigJson(String configJson) {
//         this.configJson = configJson;
//     }
// }


// package com.pixels.zapierClone.automation_platform.entity;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import jakarta.persistence.*;
// import org.hibernate.annotations.JdbcTypeCode; // Added import
// import org.hibernate.type.SqlTypes; // Added import

// @Entity
// @Table(name="actions")
// public class Action {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @JsonBackReference
//     @ManyToOne
//     @JoinColumn(name = "workflow_id")
//     private Workflow workflow;

//     @ManyToOne
//     @JoinColumn(name = "app_integration_id")
//     private AppIntegration appIntegration;

//     private String actionTypeIdentifier;
//     private String name;
//     private String description;
//     private Integer stepOrder;

//     @JdbcTypeCode(SqlTypes.JSON) // Added this line
//     @Column(columnDefinition = "jsonb")
//     private String configJson;

//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
//     public Workflow getWorkflow() { return workflow; }
//     public void setWorkflow(Workflow workflow) { this.workflow = workflow; }
//     public AppIntegration getAppIntegration() { return appIntegration; }
//     public void setAppIntegration(AppIntegration appIntegration) { this.appIntegration = appIntegration; }
//     public String getActionTypeIdentifier() { return actionTypeIdentifier; }
//     public void setActionTypeIdentifier(String actionTypeIdentifier) { this.actionTypeIdentifier = actionTypeIdentifier; }
//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }
//     public String getDescription() { return description; }
//     public void setDescription(String description) { this.description = description; }
//     public Integer getStepOrder() { return stepOrder; }
//     public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
//     public String getConfigJson() { return configJson; }
//     public void setConfigJson(String configJson) { this.configJson = configJson; }
// }

package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "actions")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @ManyToOne
    @JoinColumn(name = "app_integration_id")
    private AppIntegration appIntegration;

    private String actionTypeIdentifier;
    private String name;
    private String description;
    private Integer stepOrder;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String configJson;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Workflow getWorkflow() { return workflow; }
    public void setWorkflow(Workflow workflow) { this.workflow = workflow; }
    public AppIntegration getAppIntegration() { return appIntegration; }
    public void setAppIntegration(AppIntegration appIntegration) { this.appIntegration = appIntegration; }
    public String getActionTypeIdentifier() { return actionTypeIdentifier; }
    public void setActionTypeIdentifier(String actionTypeIdentifier) { this.actionTypeIdentifier = actionTypeIdentifier; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    public String getConfigJson() { return configJson; }
    public void setConfigJson(String configJson) { this.configJson = configJson; }
}
