package com.example.jobreport.report.service;

import com.example.jobreport.job.collector.JobCollector;
import com.example.jobreport.job.domain.JobPosting;
import com.example.jobreport.job.dto.JobPostingDto;
import com.example.jobreport.job.dto.JobRecommendationDto;
import com.example.jobreport.job.service.JobPostingService;
import com.example.jobreport.mail.service.EmailReportSender;
import com.example.jobreport.match.domain.JobMatchResult;
import com.example.jobreport.match.repository.JobMatchResultRepository;
import com.example.jobreport.match.service.JobMatchScorer;
import com.example.jobreport.report.domain.DailyReport;
import com.example.jobreport.report.domain.DailyReportItem;
import com.example.jobreport.report.domain.RecommendationLevel;
import com.example.jobreport.report.dto.DailyReportResult;
import com.example.jobreport.report.repository.DailyReportItemRepository;
import com.example.jobreport.report.repository.DailyReportRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DailyReportService {

    private static final Logger log = LoggerFactory.getLogger(DailyReportService.class);

    private final List<JobCollector> collectors;
    private final JobPostingService jobPostingService;
    private final JobMatchScorer jobMatchScorer;
    private final JobMatchResultRepository jobMatchResultRepository;
    private final DailyReportRepository dailyReportRepository;
    private final DailyReportItemRepository dailyReportItemRepository;
    private final RecommendationLevelDecider recommendationLevelDecider;
    private final HtmlReportRenderer htmlReportRenderer;
    private final EmailReportSender emailReportSender;

    public DailyReportService(List<JobCollector> collectors, JobPostingService jobPostingService,
            JobMatchScorer jobMatchScorer, JobMatchResultRepository jobMatchResultRepository,
            DailyReportRepository dailyReportRepository, DailyReportItemRepository dailyReportItemRepository,
            RecommendationLevelDecider recommendationLevelDecider, HtmlReportRenderer htmlReportRenderer,
            EmailReportSender emailReportSender) {
        this.collectors = collectors;
        this.jobPostingService = jobPostingService;
        this.jobMatchScorer = jobMatchScorer;
        this.jobMatchResultRepository = jobMatchResultRepository;
        this.dailyReportRepository = dailyReportRepository;
        this.dailyReportItemRepository = dailyReportItemRepository;
        this.recommendationLevelDecider = recommendationLevelDecider;
        this.htmlReportRenderer = htmlReportRenderer;
        this.emailReportSender = emailReportSender;
    }

    @Transactional
    public DailyReportResult generateDailyReport() {
        List<JobPostingDto> collected = collectAll();
        List<JobPosting> postings = collected.stream()
                .map(jobPostingService::saveIfNew)
                .toList();

        List<JobRecommendationDto> recommendations = postings.stream()
                .map(this::scoreAndSave)
                .sorted(Comparator.comparing((JobRecommendationDto dto) -> dto.matchResult().getTotalScore()).reversed())
                .toList();

        LocalDate reportDate = LocalDate.now();
        List<JobRecommendationDto> topRecommendations = recommendations.stream()
                .limit(5)
                .toList();
        List<JobRecommendationDto> lowPriorityItems = recommendations.stream()
                .filter(item -> item.recommendationLevel() == RecommendationLevel.LOW_PRIORITY)
                .limit(5)
                .toList();

        DailyReport report = dailyReportRepository.save(new DailyReport(
                reportDate,
                "Daily Job Recommendations - " + reportDate,
                buildSummary(collected.size(), postings.size(), topRecommendations)
        ));

        int rank = 1;
        for (JobRecommendationDto recommendation : topRecommendations) {
            dailyReportItemRepository.save(new DailyReportItem(
                    report,
                    recommendation.jobPosting(),
                    recommendation.matchResult(),
                    rank,
                    recommendation.recommendationLevel(),
                    recommendation.matchResult().getReason()
            ));
            rank++;
        }

        String htmlBody = htmlReportRenderer.render(report, withRanks(topRecommendations), lowPriorityItems);
        return new DailyReportResult(report, htmlBody);
    }

    @Transactional
    public void generateAndSendDailyReport() {
        DailyReportResult result = generateDailyReport();
        String subject = "[Job Report] Daily Job Recommendations - "
                + result.report().getReportDate().format(DateTimeFormatter.ISO_DATE);
        emailReportSender.sendHtmlReport(subject, result.htmlBody());
    }

    private List<JobPostingDto> collectAll() {
        Map<String, JobPostingDto> deduplicated = new LinkedHashMap<>();
        for (JobCollector collector : collectors) {
            List<JobPostingDto> collected = collector.collect();
            log.info("Collected {} job postings from {}", collected.size(), collector.getSource());
            for (JobPostingDto dto : collected) {
                deduplicated.putIfAbsent(deduplicationKey(dto), dto);
            }
        }
        return List.copyOf(deduplicated.values());
    }

    private String deduplicationKey(JobPostingDto dto) {
        if (dto.externalId() != null && !dto.externalId().isBlank()) {
            return dto.source() + ":external:" + dto.externalId();
        }
        return dto.source() + ":url:" + dto.url();
    }

    private JobRecommendationDto scoreAndSave(JobPosting jobPosting) {
        JobMatchResult matchResult = jobMatchResultRepository.save(jobMatchScorer.score(jobPosting));
        RecommendationLevel level = recommendationLevelDecider.decide(matchResult.getTotalScore());
        return new JobRecommendationDto(jobPosting, matchResult, level, 0);
    }

    private List<JobRecommendationDto> withRanks(List<JobRecommendationDto> recommendations) {
        int[] rank = {1};
        return recommendations.stream()
                .map(item -> new JobRecommendationDto(
                        item.jobPosting(),
                        item.matchResult(),
                        item.recommendationLevel(),
                        rank[0]++))
                .toList();
    }

    private String buildSummary(int collectedCount, int savedOrExistingCount, List<JobRecommendationDto> topRecommendations) {
        if (topRecommendations.isEmpty()) {
            return "Collected " + collectedCount + " postings, but no recommendations were available.";
        }
        JobRecommendationDto best = topRecommendations.getFirst();
        return "Collected " + collectedCount + " postings, evaluated " + savedOrExistingCount
                + " unique postings, and selected " + topRecommendations.size()
                + " top recommendations. Best match: " + best.jobPosting().getTitle()
                + " at " + best.jobPosting().getCompanyName()
                + " with score " + best.matchResult().getTotalScore() + ".";
    }
}
