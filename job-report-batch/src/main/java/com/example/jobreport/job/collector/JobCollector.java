package com.example.jobreport.job.collector;

import com.example.jobreport.job.dto.JobPostingDto;
import java.util.List;

public interface JobCollector {

    String getSource();

    List<JobPostingDto> collect();
}
