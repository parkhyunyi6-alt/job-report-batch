package com.example.jobreport.report.service;

import com.example.jobreport.report.domain.RecommendationLevel;
import org.springframework.stereotype.Component;

@Component
public class RecommendationLevelDecider {

    public RecommendationLevel decide(int score) {
        if (score >= 85) {
            return RecommendationLevel.STRONGLY_RECOMMENDED;
        }
        if (score >= 70) {
            return RecommendationLevel.RECOMMENDED;
        }
        if (score >= 50) {
            return RecommendationLevel.REVIEW;
        }
        return RecommendationLevel.LOW_PRIORITY;
    }
}
