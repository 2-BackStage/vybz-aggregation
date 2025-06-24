package com.vybz.aggregation_service.live.batch.job;

import com.vybz.aggregation_service.live.domain.LiveViewCount;
import com.vybz.aggregation_service.live.batch.reader.LiveViewCountReader;
import com.vybz.aggregation_service.live.batch.processor.LiveViewCountProcessor;
import com.vybz.aggregation_service.live.batch.writer.LiveViewCountWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LiveViewCountJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final LiveViewCountReader liveViewCountReader;
    private final LiveViewCountProcessor liveViewCountProcessor;
    private final LiveViewCountWriter liveViewCountWriter;

    @Bean
    public Job liveViewCountJob() {
        return new JobBuilder("liveViewCountJob", jobRepository)
                .start(liveViewCountStep())
                .build();
    }

    @Bean
    public Step liveViewCountStep() {
        return new StepBuilder("liveViewCountStep", jobRepository)
                .<String, LiveViewCount>chunk(100, transactionManager)
                .reader(liveViewCountReader)
                .processor(liveViewCountProcessor)
                .writer(liveViewCountWriter)
                .build();
    }
}
