package com.example.jobreport.job.repository;

import com.example.jobreport.job.domain.JobPosting;
import com.example.jobreport.job.domain.JobSource;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, UUID> {

    Optional<JobPosting> findBySourceAndExternalId(JobSource source, String externalId);

    Optional<JobPosting> findBySourceAndUrl(JobSource source, String url);
}
