package com.vybz.aggregation_service.live.batch.processor;

import com.vybz.aggregation_service.live.domain.LiveViewCount;
import com.vybz.aggregation_service.live.infrastructure.LiveViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class LiveViewCountProcessor implements ItemProcessor<String, LiveViewCount> {

    private final MongoTemplate mongoTemplate;
    private final LiveViewCountRepository liveViewCountRepository;

    @Override
    public LiveViewCount process(String streamKey) {

        long viewCount = mongoTemplate.count(
                query(where("streamKey").is(streamKey)),
                "live_view_count"
        );

        Integer beforeCount = liveViewCountRepository.findById(streamKey)
                .map(LiveViewCount::getTotalViewerCount)
                .orElse(0);

        LiveViewCount result = LiveViewCount.builder()
                .id(streamKey)
                .streamKey(streamKey)
                .totalViewerCount((int) viewCount)
                .updatedAt(Instant.now())
                .build();

        log.info("📊 [View Processor 결과] streamKey={}, beforeCount={}, nowCount={}",
                streamKey, beforeCount, viewCount);

        return result;
    }
}
