package com.example.jobreport.report.dto;

import com.example.jobreport.report.domain.DailyReport;

public record DailyReportResult(
        DailyReport report,
        String htmlBody
) {
}
