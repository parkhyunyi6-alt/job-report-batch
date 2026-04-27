package com.example.jobreport.report.service;

import com.example.jobreport.job.domain.JobPosting;
import com.example.jobreport.job.dto.JobRecommendationDto;
import com.example.jobreport.report.domain.DailyReport;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HtmlReportRenderer {

    public String render(DailyReport report, List<JobRecommendationDto> topRecommendations,
            List<JobRecommendationDto> lowPriorityItems) {
        StringBuilder html = new StringBuilder();
        html.append("""
                <!doctype html>
                <html>
                <body style="font-family:Arial,sans-serif;line-height:1.5;color:#222;">
                """);
        html.append("<h2>").append(escape(report.getTitle())).append("</h2>");
        html.append("<p><strong>Report date:</strong> ").append(report.getReportDate()).append("</p>");
        html.append("<p>").append(escape(report.getSummary())).append("</p>");
        html.append("<h3>Top 5 recommended jobs</h3>");

        if (topRecommendations.isEmpty()) {
            html.append("<p>No collected jobs were available for scoring today.</p>");
        } else {
            html.append("<ol>");
            for (JobRecommendationDto recommendation : topRecommendations) {
                JobPosting job = recommendation.jobPosting();
                html.append("<li style=\"margin-bottom:18px;\">");
                html.append("<h4 style=\"margin-bottom:4px;\">")
                        .append(escape(job.getTitle()))
                        .append(" - ")
                        .append(escape(job.getCompanyName()))
                        .append("</h4>");
                html.append("<p style=\"margin:4px 0;\">")
                        .append("<strong>Source:</strong> ").append(escape(job.getSource().name()))
                        .append(" | <strong>Location:</strong> ").append(escape(job.getLocation()))
                        .append(" | <strong>Score:</strong> ").append(recommendation.matchResult().getTotalScore())
                        .append(" | <strong>Level:</strong> ").append(recommendation.recommendationLevel())
                        .append("</p>");
                html.append("<p style=\"margin:4px 0;\"><strong>Reason:</strong> ")
                        .append(escape(recommendation.matchResult().getReason()))
                        .append("</p>");
                html.append("<p style=\"margin:4px 0;\"><strong>Risk:</strong> ")
                        .append(escape(recommendation.matchResult().getRisk()))
                        .append("</p>");
                if (job.getUrl() != null && !job.getUrl().isBlank()) {
                    html.append("<p style=\"margin:4px 0;\"><a href=\"")
                            .append(escape(job.getUrl()))
                            .append("\">Open posting</a></p>");
                }
                html.append("</li>");
            }
            html.append("</ol>");
        }

        if (!lowPriorityItems.isEmpty()) {
            html.append("<h3>Low-score or excluded summary</h3>");
            html.append("<ul>");
            for (JobRecommendationDto item : lowPriorityItems) {
                html.append("<li>")
                        .append(escape(item.jobPosting().getTitle()))
                        .append(" - ")
                        .append(escape(item.jobPosting().getCompanyName()))
                        .append(" (score ")
                        .append(item.matchResult().getTotalScore())
                        .append("): ")
                        .append(escape(item.matchResult().getRisk()))
                        .append("</li>");
            }
            html.append("</ul>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
