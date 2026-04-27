package com.example.jobreport.match.domain;

import com.example.jobreport.job.domain.JobPosting;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_match_result")
public class JobMatchResult {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Column(nullable = false)
    private int totalScore;

    @Column(nullable = false)
    private int techScore;

    @Column(nullable = false)
    private int careerScore;

    @Column(nullable = false)
    private int locationScore;

    @Column(nullable = false)
    private int companyScore;

    @Column(nullable = false)
    private int growthScore;

    @Column(columnDefinition = "text")
    private String reason;

    @Column(columnDefinition = "text")
    private String risk;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected JobMatchResult() {
    }

    public JobMatchResult(JobPosting jobPosting, int totalScore, int techScore, int careerScore, int locationScore,
            int companyScore, int growthScore, String reason, String risk) {
        this.id = UUID.randomUUID();
        this.jobPosting = jobPosting;
        this.totalScore = totalScore;
        this.techScore = techScore;
        this.careerScore = careerScore;
        this.locationScore = locationScore;
        this.companyScore = companyScore;
        this.growthScore = growthScore;
        this.reason = reason;
        this.risk = risk;
    }

    @PrePersist
    void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getReason() {
        return reason;
    }

    public String getRisk() {
        return risk;
    }
}
