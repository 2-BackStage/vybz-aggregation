package com.vybz.aggregation_service.live.batch.scheduler;

import com.vybz.aggregation_service.common.entity.BaseResponseStatus;
import com.vybz.aggregation_service.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveViewCountScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 * * * * *")
    public void runLiveViewCountJob() {
        try {
            Job job = jobRegistry.getJob("liveViewCountJob");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);

            log.info("✅ liveViewCountJob 배치 실행됨");

        } catch (Exception e) {
            log.error("❌ liveViewCountJob 배치 실행 실패", e);
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
