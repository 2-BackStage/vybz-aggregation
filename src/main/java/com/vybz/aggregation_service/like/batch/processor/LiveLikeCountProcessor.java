package com.vybz.aggregation_service.like.batch.processor;

import com.vybz.aggregation_service.like.domain.LiveLikeCount;
import com.vybz.aggregation_service.like.infrastructure.LiveLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class LiveLikeCountProcessor implements ItemProcessor<String, LiveLikeCount> {

    private final MongoTemplate mongoTemplate;
    private final LiveLikeRepository liveLikeRepository;

    @Override
    public LiveLikeCount process(String streamKey) {

        long count = mongoTemplate.count(
                Query.query(Criteria.where("streamKey").is(streamKey)),
                "live_like_count"
        );

        Integer beforeCount = liveLikeRepository.findById(streamKey)
                .map(LiveLikeCount::getTotalLikeCount)
                .orElse(0);

        LiveLikeCount result = LiveLikeCount.builder()
                .id(streamKey)
                .streamKey(streamKey)
                .totalLikeCount((int) count)
                .updatedAt(Instant.now())
                .build();

        log.info("📊 Processor 결과: streamKey={}, before={}, now={}", streamKey, beforeCount, count);
        return result;
    }
}
