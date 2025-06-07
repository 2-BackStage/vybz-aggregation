package com.vybz.aggregation_service.like.batch.job;

import com.vybz.aggregation_service.like.batch.processor.CommentLikeCountProcessor;
import com.vybz.aggregation_service.like.batch.reader.CommentLikeReader;
import com.vybz.aggregation_service.like.batch.writer.CommentLikeCountWriter;
import com.vybz.aggregation_service.like.domain.CommentLikeCount;
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
public class CommentLikeCountJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CommentLikeReader commentLikeReader;
    private final CommentLikeCountProcessor commentLikeCountProcessor;
    private final CommentLikeCountWriter commentLikeCountWriter;

    @Bean
    public Job commentLikeCountJob(){
        Step step = new StepBuilder("commentLikeCountStep", jobRepository)
                .<String, CommentLikeCount>chunk(100, platformTransactionManager)
                .reader(commentLikeReader)
                .processor(commentLikeCountProcessor)
                .writer(commentLikeCountWriter)
                .build();

        return new JobBuilder("commentLikeCountJob", jobRepository)
                .start(step)
                .build();
    }
}
