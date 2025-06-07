package com.vybz.aggregation_service.like.batch.writer;

import com.vybz.aggregation_service.like.domain.FeedLikeCount;
import com.vybz.aggregation_service.like.infrastructure.FeedLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class FeedLikeCountWriter implements ItemWriter<FeedLikeCount> {

    private final FeedLikeCountRepository feedLikeCountRepository;
    private final KafkaTemplate<String, FeedLikeCount> kafkaTemplate;

    @Override
    public void write(Chunk<? extends FeedLikeCount> items) {
        for (FeedLikeCount feedLikeCount : items) {
            feedLikeCountRepository.findByFeedId(feedLikeCount.getFeedId())
                .ifPresentOrElse(
                    existing -> {
                        existing.updateCount(feedLikeCount.getTotalLikeCount());
                        feedLikeCountRepository.save(existing);
                    },
                    () -> {
                        feedLikeCountRepository.save(feedLikeCount);
                    }
                );

        }
    }
}
