package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.kafka.event.ViewCountKafkaEvent;
import com.vybz.aggregation_service.kafka.event.LiveViewCountResultEvent;
import com.vybz.aggregation_service.kafka.producer.LiveViewCountResultEventProducer;
import com.vybz.aggregation_service.live.domain.LiveViewCount;
import com.vybz.aggregation_service.live.infrastructure.LiveViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountKafkaEventConsumer {

    private final LiveViewCountRepository liveViewCountRepository;
    private final LiveViewCountResultEventProducer liveViewCountResultEventProducer;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String LIVE_VIEW_BATCH_SET_KEY = "live:view:batch:set";

    @KafkaListener(
            topics = "live-view-count",
            groupId = "live-view-count-group",
            containerFactory = "viewCountKafkaEventKafkaListenerContainerFactory"
    )
    public void consume(ViewCountKafkaEvent event) {
        String streamKey = event.getStreamKey();

        LiveViewCount existing = liveViewCountRepository.findById(streamKey).orElse(null);
        int updatedCount = (existing != null ? existing.getTotalViewerCount() : 0) + 1;

        if (updatedCount < 1000) {
            LiveViewCount toSave = LiveViewCount.builder()
                    .id(streamKey)
                    .streamKey(streamKey)
                    .totalViewerCount(updatedCount)
                    .updatedAt(Instant.now())
                    .build();

            liveViewCountRepository.save(toSave);
            sendKafka(toSave);

            log.info("✅ 실시간 업데이트: streamKey={}, totalViewerCount={}", streamKey, updatedCount);

        } else {
            Boolean alreadyInSet = stringRedisTemplate.opsForSet().isMember(LIVE_VIEW_BATCH_SET_KEY, streamKey);

            if (alreadyInSet == null || !alreadyInSet) {
                stringRedisTemplate.opsForSet().add(LIVE_VIEW_BATCH_SET_KEY, streamKey);
                log.info("📥 배치 대상 streamKey 추가됨 → {}", streamKey);
            } else {
                log.info("⏳ 이미 배치 대상에 등록됨 → {}", streamKey);
            }
        }
    }

    private void sendKafka(LiveViewCount count) {
        LiveViewCountResultEvent event = LiveViewCountResultEvent.builder()
                .streamKey(count.getStreamKey())
                .totalViewerCount(count.getTotalViewerCount())
                .build();

        liveViewCountResultEventProducer.sendLiveViewCountEvent(event);
    }
}
