package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.kafka.event.FeedLikeCountEvent;
import com.vybz.aggregation_service.like.domain.FeedLikeCount;
import com.vybz.aggregation_service.like.infrastructure.FeedLikeCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedLikeCountEventConsumer {

    private static final String TOPIC_NAME = "feed-like-count";
    private static final String GROUP_ID = "feed-like-count-group";

    private final FeedLikeCountRepository feedLikeCountRepository;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = GROUP_ID,
            containerFactory = "feedLikeCountKafkaListenerContainerFactory"
    )
    public void consumeFeedLikeCountEvent(FeedLikeCountEvent feedLikeCountEvent) {
        log.info("🔥 Kafka 피드 좋아요 수 메시지 수신: {}", feedLikeCountEvent);
        upsertFeedLikeCount(feedLikeCountEvent);
    }


    private void updateFeedLikeCount(FeedLikeCount existing, FeedLikeCountEvent event) {
        existing.updateCount(event.getDelta());
        feedLikeCountRepository.save(existing);
        log.info("✅ 피드 좋아요 수 업데이트 완료: feedId={}, count={}", existing.getFeedId(), existing.getTotalLikeCount());
    }

    private void upsertFeedLikeCount(FeedLikeCountEvent event) {
        feedLikeCountRepository.findByFeedId(event.getFeedId())
                .ifPresentOrElse(
                        existing -> UpdateFeedLikeCount(existing, event),
                        () -> insertNewFeedLikeCount(event)
                );
    }

    private void UpdateFeedLikeCount(FeedLikeCount existing, FeedLikeCountEvent event) {
        existing.updateCount(event.getDelta());
        feedLikeCountRepository.save(existing);
        log.info("✅ 피드 좋아요 수 업데이트 완료: feedId={}, count={}", existing.getFeedId(), existing.getTotalLikeCount());
    }

    private void insertNewFeedLikeCount(FeedLikeCountEvent event) {
        FeedLikeCount newCount = FeedLikeCount.builder()
                .feedId(event.getFeedId())
                .totalLikeCount(Math.max(0, event.getDelta()))
                .updatedAt(Instant.now())
                .build();

        feedLikeCountRepository.save(newCount);
        log.info("🆕 피드 좋아요 수 새로 저장: feedId={}, count={}", newCount.getFeedId(), newCount.getTotalLikeCount());
    }

}
