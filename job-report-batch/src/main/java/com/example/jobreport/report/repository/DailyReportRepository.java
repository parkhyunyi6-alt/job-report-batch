package com.example.jobreport.report.repository;

import com.example.jobreport.report.domain.DailyReport;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyReportRepository extends JpaRepository<DailyReport, UUID> {
}
