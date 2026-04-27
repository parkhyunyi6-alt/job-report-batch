package com.example.jobreport.report.repository;

import com.example.jobreport.report.domain.DailyReportItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyReportItemRepository extends JpaRepository<DailyReportItem, UUID> {
}
