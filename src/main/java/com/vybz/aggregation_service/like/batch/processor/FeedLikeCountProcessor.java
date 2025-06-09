package com.vybz.aggregation_service.like.batch.processor;


import com.vybz.aggregation_service.like.batch.policy.FeedLikeCountDisplayPolicy;
import com.vybz.aggregation_service.like.domain.FeedLikeCount;
import com.vybz.aggregation_service.like.domain.FeedType;
import com.vybz.aggregation_service.like.infrastructure.FeedLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@StepScope
@RequiredArgsConstructor
public class FeedLikeCountProcessor implements ItemProcessor<String, FeedLikeCount> {

    private final FeedLikeCountRepository feedLikeCountRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public FeedLikeCount process(String feedId){
        long count = mongoTemplate.count(
                Query.query(Criteria.where("feedId").is(feedId)),
                "feed_like"
        );

        String display = FeedLikeCountDisplayPolicy.convert(count);

        FeedType feedType = feedLikeCountRepository.findByFeedId(feedId)
                .map(FeedLikeCount::getFeedType)
                .orElse(null);

        return FeedLikeCount.builder()
                .feedId(feedId)
                .feedType(feedType)
                .totalLikeCount((int)count)
                .displayLikeCount(display)
                .updatedAt(Instant.now())
                .build();
    }
}
