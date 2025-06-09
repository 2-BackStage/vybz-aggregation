package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.kafka.event.FeedLikeDeltaEvent;
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
public class FeedLikeDeltaEventConsumer {

    private static final String TOPIC_NAME = "feed-delta-count";
    private static final String GROUP_ID = "feed-like-delta-group";

    private final FeedLikeCountRepository feedLikeCountRepository;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = GROUP_ID,
            containerFactory = "feedLikeDeltaEventConcurrentKafkaListenerContainerFactory"
    )
    public void consumeFeedLikeDeltaEvent(FeedLikeDeltaEvent event) {
        if (event.getDelta() == 0) {
            log.info("🚫 [무시] delta=0 이벤트: feedId={}", event.getFeedId());
            return;
        }

        log.info("🔥 Kafka 피드 좋아요 수 메시지 수신: {}", event);
        upsertFeedLikeDelta(event);
    }

    private void upsertFeedLikeDelta(FeedLikeDeltaEvent event) {
        feedLikeCountRepository.findByFeedId(event.getFeedId())
                .ifPresentOrElse(
                        existing -> updateFeedLikeDelta(existing, event),
                        () -> insertNewFeedLikeCount(event)
                );
    }

    private void updateFeedLikeDelta(FeedLikeCount existing, FeedLikeDeltaEvent event) {
        existing.updateCount(event.getDelta());
        feedLikeCountRepository.save(existing);
        log.info("✅ 피드 좋아요 수 업데이트 완료: feedId={}, count={}", existing.getFeedId(), existing.getTotalLikeCount());
    }

    private void insertNewFeedLikeCount(FeedLikeDeltaEvent event) {
        FeedLikeCount newCount = FeedLikeCount.builder()
                .feedId(event.getFeedId())
                .feedType(event.getFeedType())
                .totalLikeCount(Math.max(0, event.getDelta()))
                .updatedAt(Instant.now())
                .build();

        feedLikeCountRepository.save(newCount);
        log.info("🆕 피드 좋아요 수 새로 저장: feedId={}, count={}", newCount.getFeedId(), newCount.getTotalLikeCount());
    }
}
