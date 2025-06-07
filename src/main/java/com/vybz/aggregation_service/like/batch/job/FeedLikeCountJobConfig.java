package com.vybz.aggregation_service.like.batch.job;

import com.vybz.aggregation_service.like.batch.processor.FeedLikeCountProcessor;
import com.vybz.aggregation_service.like.batch.reader.FeedLikeReader;
import com.vybz.aggregation_service.like.batch.writer.FeedLikeCountWriter;
import com.vybz.aggregation_service.like.domain.FeedLikeCount;
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
public class FeedLikeCountJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final FeedLikeReader feedLikeReader;
    private final FeedLikeCountProcessor feedLikeCountProcessor;
    private final FeedLikeCountWriter feedLikeCountWriter;

    @Bean
    public Job feedLikeCountJob(){
        Step step = new StepBuilder("feedLikeCountStep", jobRepository)
                .<String, FeedLikeCount> chunk(100, platformTransactionManager)
                .reader(feedLikeReader)
                .processor(feedLikeCountProcessor)
                .writer(feedLikeCountWriter)
                .build();

        return new JobBuilder("feedLikeCountJob", jobRepository)
                .start(step)
                .build();
    }
}
