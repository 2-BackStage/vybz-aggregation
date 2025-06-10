package com.vybz.aggregation_service.follow.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowCountBatchScheduler {

    @Qualifier("followAggregationJob")
    private final Job followAggregationJob;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedRate = 60000)
    public void runBuskerFollowerCountJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            log.info("⏳ BuskerFollowerCount 배치 실행 시작");
            jobLauncher.run(followAggregationJob, jobParameters);
            log.info("✅ BuskerFollowerCount 배치 실행 완료");
        } catch (Exception e) {
            log.error("❌ BuskerFollowerCount 배치 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }

}
