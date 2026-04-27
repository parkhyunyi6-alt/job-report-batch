package com.example.jobreport.report.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "daily_report")
public class DailyReport {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected DailyReport() {
    }

    public DailyReport(LocalDate reportDate, String title, String summary) {
        this.id = UUID.randomUUID();
        this.reportDate = reportDate;
        this.title = title;
        this.summary = summary;
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }
}
