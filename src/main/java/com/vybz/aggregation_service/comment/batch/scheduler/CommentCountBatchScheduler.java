package com.vybz.aggregation_service.comment.batch.scheduler;

import com.vybz.aggregation_service.common.entity.BaseResponseStatus;
import com.vybz.aggregation_service.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCountBatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 * * * * *")

    public void runCommentCountJob(){
        try {
            Job job = jobRegistry.getJob("commentCountJob");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.COMMENT_BATCH_FAIL);
        }
    }

}
