//package com.vybz.aggregation_service.follow.application.scheduler;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class FollowBatchScheduler {
//
//    private final JobLauncher jobLauncher;
//    private final Job userFollowCountJob;
//    private final Job buskerFollowerCountJob;
//
//    @Scheduled(fixedRate = 30000)
//    public void runFollowBatch() throws Exception {
//        JobParameters params = new JobParametersBuilder()
//                .addLong("time", System.currentTimeMillis())
//                .toJobParameters();
//
//        jobLauncher.run(userFollowCountJob, params);
//        jobLauncher.run(buskerFollowerCountJob, params);
//    }
//
//}
