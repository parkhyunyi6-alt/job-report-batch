package com.example.jobreport.job.collector;

import com.example.jobreport.job.domain.EmploymentType;
import com.example.jobreport.job.domain.JobSource;
import com.example.jobreport.job.dto.JobPostingDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SaraminJobCollector implements JobCollector {

    private final Environment environment;

    public SaraminJobCollector(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getSource() {
        return JobSource.SARAMIN.name();
    }

    @Override
    public List<JobPostingDto> collect() {
        String apiKey = environment.getProperty("SARAMIN_API_KEY");
        if (!StringUtils.hasText(apiKey)) {
            apiKey = environment.getProperty("SARANMIN_API_KEY");
        }
        if (StringUtils.hasText(apiKey)) {
            // TODO: Integrate Saramin's official API client here. Do not scrape pages.
            return mockPostings();
        }

        // TODO: Add future integrations through saved URLs, email alerts, or official APIs.
        // LinkedIn and JobPlanet must not be scraped.
        return mockPostings();
    }

    private List<JobPostingDto> mockPostings() {
        LocalDateTime now = LocalDateTime.now();
        return List.of(
                new JobPostingDto(
                        JobSource.SARAMIN,
                        "mock-saramin-001",
                        "Java/Spring Backend Developer",
                        "Commerce Platform Labs",
                        "Seoul Gangnam-gu",
                        5,
                        8,
                        EmploymentType.FULL_TIME,
                        "Java, Spring Boot, PostgreSQL, AWS, React",
                        "Product backend role for commerce platform services, API design, and system improvement.",
                        "https://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=mock001",
                        now.minusDays(1),
                        now.plusDays(20)
                ),
                new JobPostingDto(
                        JobSource.SARAMIN,
                        "mock-saramin-002",
                        "Smart Factory MES Solution Developer",
                        "Manufacturing IT Group",
                        "Seongnam-si, Gyeonggi-do",
                        6,
                        10,
                        EmploymentType.FULL_TIME,
                        "Java, Spring MVC, Oracle, MES, React",
                        "Develop MES and factory monitoring systems for enterprise manufacturing customers.",
                        "https://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=mock002",
                        now.minusDays(2),
                        now.plusDays(14)
                ),
                new JobPostingDto(
                        JobSource.SARAMIN,
                        "mock-saramin-003",
                        "Full-stack Developer for K-pop Fan Platform",
                        "Content Connect",
                        "Seoul Mapo-gu Hybrid",
                        4,
                        7,
                        EmploymentType.FULL_TIME,
                        "Java, Spring, React, SQL, AWS",
                        "Build fan commerce and content platform features with a small product engineering team.",
                        "https://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=mock003",
                        now.minusDays(1),
                        now.plusDays(30)
                ),
                new JobPostingDto(
                        JobSource.SARAMIN,
                        "mock-saramin-004",
                        "Legacy SI Maintenance Engineer",
                        "Unknown SI Partner",
                        "Daejeon",
                        2,
                        4,
                        EmploymentType.CONTRACT,
                        "C#, .NET Framework, Oracle",
                        "Operate and maintain legacy internal systems with fixed customer requirements.",
                        "https://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=mock004",
                        now.minusDays(3),
                        now.plusDays(10)
                ),
                new JobPostingDto(
                        JobSource.SARAMIN,
                        "mock-saramin-005",
                        "Backend Engineer, Beauty Commerce",
                        "Beauty Growth",
                        "Seoul Seocho-gu",
                        5,
                        9,
                        EmploymentType.FULL_TIME,
                        "Java, Spring Boot, MySQL, PostgreSQL, AWS",
                        "Own backend domains for beauty commerce, order, catalog, and campaign services.",
                        "https://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=mock005",
                        now,
                        now.plusDays(25)
                ),
                new JobPostingDto(
                        JobSource.SARAMIN,
                        "mock-saramin-006",
                        "Senior Architect",
                        "Global Enterprise",
                        "Seoul",
                        11,
                        15,
                        EmploymentType.FULL_TIME,
                        "Java, Spring, Kubernetes, AWS, SQL",
                        "Lead enterprise architecture and platform modernization across group affiliates.",
                        "https://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=mock006",
                        now.minusDays(4),
                        now.plusDays(12)
                )
        );
    }
}
