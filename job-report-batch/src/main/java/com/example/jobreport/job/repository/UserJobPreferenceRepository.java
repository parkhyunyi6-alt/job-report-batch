package com.example.jobreport.job.repository;

import com.example.jobreport.job.domain.UserJobPreference;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJobPreferenceRepository extends JpaRepository<UserJobPreference, UUID> {
}
