package com.example.jobreport.job.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_posting")
public class JobPosting {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobSource source;

    private String externalId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false)
    private String companyName;

    private String location;

    private Integer careerMin;

    private Integer careerMax;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(columnDefinition = "text")
    private String techStack;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String url;

    private LocalDateTime postedAt;

    private LocalDateTime deadlineAt;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected JobPosting() {
    }

    public JobPosting(JobSource source, String externalId, String title, String companyName, String location,
            Integer careerMin, Integer careerMax, EmploymentType employmentType, String techStack,
            String description, String url, LocalDateTime postedAt, LocalDateTime deadlineAt,
            LocalDateTime collectedAt) {
        this.id = UUID.randomUUID();
        this.source = source;
        this.externalId = externalId;
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.careerMin = careerMin;
        this.careerMax = careerMax;
        this.employmentType = employmentType;
        this.techStack = techStack;
        this.description = description;
        this.url = url;
        this.postedAt = postedAt;
        this.deadlineAt = deadlineAt;
        this.collectedAt = collectedAt;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = now;
        updatedAt = now;
        if (collectedAt == null) {
            collectedAt = now;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public JobSource getSource() {
        return source;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCareerMin() {
        return careerMin;
    }

    public Integer getCareerMax() {
        return careerMax;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public String getTechStack() {
        return techStack;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public LocalDateTime getDeadlineAt() {
        return deadlineAt;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }
}
