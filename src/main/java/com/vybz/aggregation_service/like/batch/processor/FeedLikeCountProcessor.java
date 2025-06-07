package com.vybz.aggregation_service.like.batch.processor;

import com.vybz.aggregation_service.like.batch.policy.FeedLikeCountDisplayPolicy;
import com.vybz.aggregation_service.like.domain.FeedLikeCount;
import com.vybz.aggregation_service.like.infrastructure.FeedLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class FeedLikeCountProcessor implements ItemProcessor<String, FeedLikeCount> {

    private final FeedLikeCountRepository feedLikeCountRepository;

    @Override
    public FeedLikeCount process(String feedId){
        int count = (int) feedLikeCountRepository.countByFeedId(feedId);

        String display = FeedLikeCountDisplayPolicy.convert(count);

        return FeedLikeCount.builder()
                .feedId(feedId)
                .totalLikeCount(count)
                .displayLikeCount(display)
                .updatedAt(java.time.Instant.now())
                .build();
    }
}
