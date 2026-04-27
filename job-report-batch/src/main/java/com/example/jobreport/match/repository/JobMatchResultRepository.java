package com.example.jobreport.match.repository;

import com.example.jobreport.match.domain.JobMatchResult;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobMatchResultRepository extends JpaRepository<JobMatchResult, UUID> {
}
