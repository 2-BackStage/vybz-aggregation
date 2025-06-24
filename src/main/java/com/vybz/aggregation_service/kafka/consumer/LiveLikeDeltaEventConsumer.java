package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.kafka.event.LiveLikeCountResultEvent;
import com.vybz.aggregation_service.kafka.event.LiveLikeDeltaEvent;
import com.vybz.aggregation_service.kafka.producer.LiveLikeCountResultEventProducer;
import com.vybz.aggregation_service.like.domain.LiveLikeCount;
import com.vybz.aggregation_service.like.infrastructure.LiveLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
@Slf4j
@Component
@RequiredArgsConstructor
public class LiveLikeDeltaEventConsumer {

    private final LiveLikeRepository liveLikeRepository;
    private final LiveLikeCountResultEventProducer liveLikeCountResultEventProducer;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String LIVE_LIKE_BATCH_SET_KEY = "live:like:batch:set";

    @KafkaListener(
            topics = "live-like-delta-count",
            groupId = "live-like-delta-group",
            containerFactory = "liveLikeDeltaEventKafkaListenerContainerFactory"
    )
    public void consume(LiveLikeDeltaEvent event) {
        String streamKey = event.getStreamKey();

        LiveLikeCount existing = liveLikeRepository.findById(streamKey).orElse(null);

        int updatedCount = existing != null ? existing.getTotalLikeCount() + 1 : 1;

        if (updatedCount < 1000) {
            LiveLikeCount toSave = LiveLikeCount.builder()
                    .id(streamKey)
                    .streamKey(streamKey)
                    .totalLikeCount(updatedCount)
                    .updatedAt(Instant.now())
                    .build();

            liveLikeRepository.save(toSave);
            sendKafka(toSave);
            log.info("✅ 실시간 업데이트: streamKey={}, totalLikeCount={}", streamKey, updatedCount);

        } else {
            Boolean alreadyInSet = stringRedisTemplate.opsForSet().isMember("live:like:batch:queue", streamKey);
            if (alreadyInSet == null || !alreadyInSet) {
                stringRedisTemplate.opsForSet().add("live:like:batch:queue", streamKey);
                log.info("📥 배치 대상 streamKey 추가됨 → {}", streamKey);
            } else {
                log.info("⏳ 이미 배치 대상에 등록됨 → {}", streamKey);
            }
        }
    }

    private void sendKafka(LiveLikeCount count) {
        LiveLikeCountResultEvent event = LiveLikeCountResultEvent.builder()
                .streamKey(count.getStreamKey())
                .totalLikeCount(count.getTotalLikeCount())
                .build();

        liveLikeCountResultEventProducer.sendLiveLikeCountEvent(event);
    }
}
