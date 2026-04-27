package com.example.jobreport.match.service;

import com.example.jobreport.job.domain.JobPosting;
import com.example.jobreport.match.domain.JobMatchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class JobMatchScorer {

    public JobMatchResult score(JobPosting jobPosting) {
        String text = normalize(String.join(" ",
                nullToEmpty(jobPosting.getTitle()),
                nullToEmpty(jobPosting.getCompanyName()),
                nullToEmpty(jobPosting.getLocation()),
                nullToEmpty(jobPosting.getTechStack()),
                nullToEmpty(jobPosting.getDescription())
        ));

        int techScore = scoreTech(text);
        int careerScore = scoreCareer(jobPosting);
        int locationScore = scoreLocation(text);
        int companyScore = scoreCompany(text, jobPosting.getCompanyName());
        int growthScore = scoreGrowth(text);
        int totalScore = techScore + careerScore + locationScore + companyScore + growthScore;

        return new JobMatchResult(
                jobPosting,
                Math.min(100, totalScore),
                techScore,
                careerScore,
                locationScore,
                companyScore,
                growthScore,
                buildReason(jobPosting, techScore, careerScore, locationScore, growthScore),
                buildRisk(jobPosting, careerScore, locationScore, growthScore, text)
        );
    }

    private int scoreTech(String text) {
        int score = 0;
        if (containsAny(text, "java")) {
            score += 8;
        }
        if (containsAny(text, "spring", "spring boot", "spring mvc")) {
            score += 8;
        }
        if (containsAny(text, "react")) {
            score += 4;
        }
        if (containsAny(text, "postgresql", "postgres", "sql", "oracle", "mysql")) {
            score += 3;
        }
        if (containsAny(text, "aws")) {
            score += 2;
        }
        return Math.min(25, score);
    }

    private int scoreCareer(JobPosting jobPosting) {
        Integer min = jobPosting.getCareerMin();
        Integer max = jobPosting.getCareerMax();
        if (min == null && max == null) {
            return 12;
        }
        int lower = min == null ? 0 : min;
        int upper = max == null ? lower : max;
        if (lower <= 7 && upper >= 7) {
            return 20;
        }
        if (lower >= 5 && lower <= 8) {
            return 18;
        }
        if (upper < 5) {
            return 8;
        }
        if (lower > 10) {
            return 6;
        }
        return 13;
    }

    private int scoreLocation(String text) {
        if (containsAny(text, "seoul", "서울")) {
            return 15;
        }
        if (containsAny(text, "gyeonggi", "경기", "seongnam", "pangyo", "bundang", "incheon", "성남", "판교", "분당", "인천")) {
            return 10;
        }
        if (containsAny(text, "remote", "hybrid", "재택", "하이브리드")) {
            return 8;
        }
        return 4;
    }

    private int scoreCompany(String text, String companyName) {
        if (containsAny(text, "enterprise", "group affiliate", "global", "대기업", "계열사")) {
            return 14;
        }
        if (containsAny(text, "platform", "labs", "commerce", "content", "growth")) {
            return 11;
        }
        if (companyName == null || companyName.toLowerCase(Locale.ROOT).contains("unknown")) {
            return 7;
        }
        return 9;
    }

    private int scoreGrowth(String text) {
        if (containsAny(text, "backend", "spring boot", "product", "service", "platform", "system design", "api design")) {
            return 24;
        }
        if (containsAny(text, "smart factory", "mes", "manufacturing", "factory monitoring", "manufacturing it")) {
            return 20;
        }
        if (containsAny(text, "maintenance only", "legacy", "operate and maintain", "fixed customer")) {
            return 7;
        }
        if (containsAny(text, "si")) {
            return 12;
        }
        return 14;
    }

    private String buildReason(JobPosting jobPosting, int techScore, int careerScore, int locationScore, int growthScore) {
        List<String> reasons = new ArrayList<>();
        if (techScore >= 18) {
            reasons.add("Java/Spring 중심 목표와 기존 React, SQL 경험을 함께 활용할 수 있습니다");
        }
        if (careerScore >= 18) {
            reasons.add("7년차 경력 범위와 잘 맞습니다");
        }
        if (locationScore >= 10) {
            reasons.add("선호 지역인 서울 또는 수도권 조건에 가깝습니다");
        }
        if (growthScore >= 20) {
            reasons.add("SI 경험을 서비스 백엔드, 플랫폼, 시스템 설계 방향으로 확장하기 좋습니다");
        }
        if (containsAny(normalize(nullToEmpty(jobPosting.getDescription())), "mes", "smart factory", "manufacturing")) {
            reasons.add("LG CNS Smart Factory Solutions Team의 MES/모니터링 경험과 직접 연결됩니다");
        }
        if (reasons.isEmpty()) {
            reasons.add("일부 기술 또는 도메인 경험이 맞지만 추가 검토가 필요합니다");
        }
        return String.join(". ", reasons) + ".";
    }

    private String buildRisk(JobPosting jobPosting, int careerScore, int locationScore, int growthScore, String text) {
        List<String> risks = new ArrayList<>();
        if (careerScore <= 8) {
            risks.add("요구 경력이 현재 경력보다 낮거나 높아 성장 폭이 제한될 수 있습니다");
        }
        if (locationScore < 8) {
            risks.add("선호 지역과 거리가 있을 수 있습니다");
        }
        if (growthScore <= 12) {
            risks.add("반복 SI 운영/유지보수 성격이면 서비스 백엔드 전환에 덜 유리할 수 있습니다");
        }
        if (!containsAny(text, "java", "spring")) {
            risks.add("Java/Spring 비중이 낮을 수 있습니다");
        }
        if (risks.isEmpty()) {
            risks.add("채용공고의 팀 문화와 실제 개발 자율성은 면접에서 확인이 필요합니다");
        }
        return String.join(". ", risks) + ".";
    }

    private boolean containsAny(String text, String... words) {
        for (String word : words) {
            if (text.contains(word.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
