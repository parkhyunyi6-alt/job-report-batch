package com.example.jobreport.job.service;

import com.example.jobreport.job.domain.JobPosting;
import com.example.jobreport.job.dto.JobPostingDto;
import com.example.jobreport.job.repository.JobPostingRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    @Transactional
    public JobPosting saveIfNew(JobPostingDto dto) {
        Optional<JobPosting> existing = findExisting(dto);
        return existing.orElseGet(() -> jobPostingRepository.save(toEntity(dto)));
    }

    private Optional<JobPosting> findExisting(JobPostingDto dto) {
        if (StringUtils.hasText(dto.externalId())) {
            Optional<JobPosting> byExternalId = jobPostingRepository.findBySourceAndExternalId(
                    dto.source(), dto.externalId());
            if (byExternalId.isPresent()) {
                return byExternalId;
            }
        }
        if (StringUtils.hasText(dto.url())) {
            return jobPostingRepository.findBySourceAndUrl(dto.source(), dto.url());
        }
        return Optional.empty();
    }

    private JobPosting toEntity(JobPostingDto dto) {
        return new JobPosting(
                dto.source(),
                dto.externalId(),
                dto.title(),
                dto.companyName(),
                dto.location(),
                dto.careerMin(),
                dto.careerMax(),
                dto.employmentType(),
                dto.techStack(),
                dto.description(),
                dto.url(),
                dto.postedAt(),
                dto.deadlineAt(),
                LocalDateTime.now()
        );
    }
}
