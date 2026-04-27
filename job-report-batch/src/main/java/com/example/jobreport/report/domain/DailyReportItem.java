package com.example.jobreport.report.domain;

import com.example.jobreport.job.domain.JobPosting;
import com.example.jobreport.match.domain.JobMatchResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "daily_report_item")
public class DailyReportItem {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "daily_report_id", nullable = false)
    private DailyReport dailyReport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_match_result_id", nullable = false)
    private JobMatchResult jobMatchResult;

    @Column(nullable = false)
    private int rankNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationLevel recommendationLevel;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected DailyReportItem() {
    }

    public DailyReportItem(DailyReport dailyReport, JobPosting jobPosting, JobMatchResult jobMatchResult,
            int rankNo, RecommendationLevel recommendationLevel, String comment) {
        this.id = UUID.randomUUID();
        this.dailyReport = dailyReport;
        this.jobPosting = jobPosting;
        this.jobMatchResult = jobMatchResult;
        this.rankNo = rankNo;
        this.recommendationLevel = recommendationLevel;
        this.comment = comment;
    }

    @PrePersist
    void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = LocalDateTime.now();
    }
}
