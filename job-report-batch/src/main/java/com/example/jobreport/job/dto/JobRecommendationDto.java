package com.example.jobreport.job.dto;

import com.example.jobreport.job.domain.JobPosting;
import com.example.jobreport.match.domain.JobMatchResult;
import com.example.jobreport.report.domain.RecommendationLevel;

public record JobRecommendationDto(
        JobPosting jobPosting,
        JobMatchResult matchResult,
        RecommendationLevel recommendationLevel,
        int rankNo
) {
}
