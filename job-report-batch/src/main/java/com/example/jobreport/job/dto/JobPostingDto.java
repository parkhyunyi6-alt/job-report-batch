package com.example.jobreport.job.dto;

import com.example.jobreport.job.domain.EmploymentType;
import com.example.jobreport.job.domain.JobSource;
import java.time.LocalDateTime;

public record JobPostingDto(
        JobSource source,
        String externalId,
        String title,
        String companyName,
        String location,
        Integer careerMin,
        Integer careerMax,
        EmploymentType employmentType,
        String techStack,
        String description,
        String url,
        LocalDateTime postedAt,
        LocalDateTime deadlineAt
) {
}
