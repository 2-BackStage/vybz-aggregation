package com.vybz.aggregation_service.comment.batch.job;

import com.vybz.aggregation_service.comment.batch.processor.CommentCountProcessor;
import com.vybz.aggregation_service.comment.batch.reader.CommentFeedIdReader;
import com.vybz.aggregation_service.comment.batch.writer.CommentCountWriter;
import com.vybz.aggregation_service.comment.domain.CommentCount;
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
public class CommentCountBatchJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CommentFeedIdReader commentFeedIdReader;
    private final CommentCountProcessor commentCountProcessor;
    private final CommentCountWriter commentCountWriter;

    @Bean
    public Job commentCountJob(){
        Step step = new StepBuilder("commentCountStep", jobRepository)
                .<String, CommentCount> chunk(100, platformTransactionManager)
                .reader(commentFeedIdReader)
                .processor(commentCountProcessor)
                .writer(commentCountWriter)
                .build();

        return new JobBuilder("commentCountJob", jobRepository)
                .start(step)
                .build();




    }


}
