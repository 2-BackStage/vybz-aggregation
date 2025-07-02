package com.vybz.aggregation_service.follow.batch.job;

import com.vybz.aggregation_service.follow.batch.dto.BuskerFollowerCountDto;
import com.vybz.aggregation_service.follow.batch.dto.UserFollowingCountDto;
import com.vybz.aggregation_service.follow.batch.processor.BuskerFollowerCountProcessor;
import com.vybz.aggregation_service.follow.batch.processor.UserFollowingCountProcessor;
import com.vybz.aggregation_service.follow.batch.reader.BuskerFollowerCountReader;
import com.vybz.aggregation_service.follow.batch.reader.UserFollowingCountReader;
import com.vybz.aggregation_service.follow.batch.writer.BuskerFollowerCountWriter;
import com.vybz.aggregation_service.follow.batch.writer.UserFollowingCountWriter;
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
public class FollowCountBatchJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final BuskerFollowerCountReader buskerReader;
    private final BuskerFollowerCountProcessor buskerProcessor;
    private final BuskerFollowerCountWriter buskerWriter;

    private final UserFollowingCountReader userReader;
    private final UserFollowingCountProcessor userProcessor;
    private final UserFollowingCountWriter userWriter;

    @Bean
    public Job followAggregationJob() {
        return new JobBuilder("followAggregationJob", jobRepository)
                .start(buskerFollowerCountStep())
                .next(userFollowingCountStep())
                .build();
    }

    @Bean
    public Step buskerFollowerCountStep() {
        return new StepBuilder("buskerFollowerCountStep", jobRepository)
                .<String, BuskerFollowerCountDto>chunk(200, transactionManager)
                .reader(buskerReader)
                .processor(buskerProcessor)
                .writer(buskerWriter)
                .build();
    }

    @Bean
    public Step userFollowingCountStep() {
        return new StepBuilder("userFollowingCountStep", jobRepository)
                .<String, UserFollowingCountDto>chunk(200, transactionManager)
                .reader(userReader)
                .processor(userProcessor)
                .writer(userWriter)
                .build();
    }

}
