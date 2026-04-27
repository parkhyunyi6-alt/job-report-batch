package com.example.jobreport.job.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_job_preference")
public class UserJobPreference {

    @Id
    private UUID id;

    @Column(columnDefinition = "text")
    private String preferredLocations;

    @Column(columnDefinition = "text")
    private String preferredTechStack;

    @Column(columnDefinition = "text")
    private String preferredDomains;

    @Column(columnDefinition = "text")
    private String excludedKeywords;

    private Integer minCareerYears;

    private Integer maxCareerYears;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected UserJobPreference() {
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
