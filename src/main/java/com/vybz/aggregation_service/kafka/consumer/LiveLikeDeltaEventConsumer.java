package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.kafka.event.LiveLikeDeltaEvent;
import com.vybz.aggregation_service.like.domain.LiveLikeCount;
import com.vybz.aggregation_service.like.infrastructure.LiveLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveLikeDeltaEventConsumer {

    private final LiveLikeRepository liveLikeRepository;

    @KafkaListener(
            topics = "live-like-delta-count",
            groupId = "live-like-delta-group",
            containerFactory = "liveLikeDeltaEventKafkaListenerContainerFactory"
    )
    public void consume(LiveLikeDeltaEvent event) {
        String streamKey = event.getStreamKey();

        LiveLikeCount existing = liveLikeRepository.findById(streamKey).orElse(null);

        if (existing != null) {
            LiveLikeCount updated = LiveLikeCount.builder()
                    .id(existing.getId())
                    .streamKey(existing.getStreamKey())
                    .totalLikeCount(existing.getTotalLikeCount() + 1)
                    .updatedAt(Instant.now())
                    .build();

            liveLikeRepository.save(updated);
            log.info("🔁 좋아요 수 증가: {} → {}", streamKey, updated.getTotalLikeCount());

        } else {
            LiveLikeCount created = LiveLikeCount.builder()
                    .id(streamKey)
                    .streamKey(streamKey)
                    .totalLikeCount(1)
                    .updatedAt(Instant.now())
                    .build();

            liveLikeRepository.save(created);
            log.info("🆕 좋아요 수 최초 생성: {} → 1", streamKey);
        }
    }
}
