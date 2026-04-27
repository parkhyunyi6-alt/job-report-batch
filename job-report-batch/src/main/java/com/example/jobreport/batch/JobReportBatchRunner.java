package com.example.jobreport.batch;

import com.example.jobreport.report.service.DailyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JobReportBatchRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(JobReportBatchRunner.class);

    private final DailyReportService dailyReportService;
    private final ApplicationContext applicationContext;

    public JobReportBatchRunner(DailyReportService dailyReportService, ApplicationContext applicationContext) {
        this.dailyReportService = dailyReportService;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        int exitCode = 0;
        try {
            log.info("Daily job report batch started");
            dailyReportService.generateAndSendDailyReport();
            log.info("Daily job report batch completed");
        } catch (Exception ex) {
            exitCode = 1;
            log.error("Daily job report batch failed", ex);
        } finally {
            int springExitCode = SpringApplication.exit(applicationContext, () -> exitCode);
            System.exit(springExitCode);
        }
    }
}
