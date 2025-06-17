package com.vybz.aggregation_service.like.batch.job;

import com.vybz.aggregation_service.like.domain.LiveLikeCount;
import com.vybz.aggregation_service.like.batch.reader.LiveLikeReader;
import com.vybz.aggregation_service.like.batch.processor.LiveLikeCountProcessor;
import com.vybz.aggregation_service.like.batch.writer.LiveLikeCountWriter;
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
public class LiveLikeCountJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final LiveLikeReader liveLikeReader;
    private final LiveLikeCountProcessor liveLikeCountProcessor;
    private final LiveLikeCountWriter liveLikeCountWriter;

    @Bean
    public Job liveLikeCountJob() {
        Step step = new StepBuilder("liveLikeCountStep", jobRepository)
                .<String, LiveLikeCount>chunk(100, platformTransactionManager)
                .reader(liveLikeReader)
                .processor(liveLikeCountProcessor)
                .writer(liveLikeCountWriter)
                .build();

        return new JobBuilder("liveLikeCountJob", jobRepository)
                .start(step)
                .build();
    }
}
