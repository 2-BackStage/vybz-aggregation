package com.vybz.aggregation_service.live.batch.writer;

import com.vybz.aggregation_service.kafka.event.LiveViewCountResultEvent;
import com.vybz.aggregation_service.kafka.producer.LiveViewCountResultEventProducer;
import com.vybz.aggregation_service.live.domain.LiveViewCount;
import com.vybz.aggregation_service.live.infrastructure.LiveViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class LiveViewCountWriter implements ItemWriter<LiveViewCount> {

    private final LiveViewCountRepository liveViewCountRepository;
    private final LiveViewCountResultEventProducer liveViewCountResultEventProducer;

    @Override
    public void write(Chunk<? extends LiveViewCount> items) {
        log.info("🖊️ Writer 시작 - 총 {}건 처리 예정", items.size());

        for (LiveViewCount newCount : items) {
            String streamKey = newCount.getStreamKey();
            Integer incomingCount = newCount.getTotalViewerCount();
            String updatedAt = newCount.getUpdatedAt().toString();

            log.info("➡️ 처리 중: streamKey={}, incomingCount={}, updatedAt={}", streamKey, incomingCount, updatedAt);

            liveViewCountRepository.findById(streamKey)
                    .ifPresentOrElse(
                            existing -> {
                                int before = existing.getTotalViewerCount();
                                int after = newCount.getTotalViewerCount();

                                log.info("🔄 기존 값 발견: streamKey={}, 기존 count={}, 새로운 count={}", streamKey, before, after);

                                LiveViewCount updated = LiveViewCount.builder()
                                        .id(existing.getId())
                                        .streamKey(existing.getStreamKey())
                                        .totalViewerCount(after)
                                        .updatedAt(newCount.getUpdatedAt())
                                        .build();

                                liveViewCountRepository.save(updated);
                                log.info("✅ 업데이트 완료: streamKey={}, 최종 count={}, 저장 시각={}", streamKey, after, newCount.getUpdatedAt());
                                sendKafka(updated);
                            },
                            () -> {
                                liveViewCountRepository.save(newCount);
                                log.info("🆕 신규 저장 완료: streamKey={}, count={}, 저장 시각={}", streamKey, incomingCount, updatedAt);
                                sendKafka(newCount);
                            }
                    );
        }

        log.info("✅ Writer 완료 - 전체 {}건 저장 처리 성공", items.size());
    }

    private void sendKafka(LiveViewCount count) {
        LiveViewCountResultEvent event = LiveViewCountResultEvent.builder()
                .streamKey(count.getStreamKey())
                .totalViewerCount(count.getTotalViewerCount())
                .build();

        liveViewCountResultEventProducer.sendLiveViewCountEvent(event);
        log.info("📤 Kafka 전송 완료: streamKey={}, totalViewerCount={}", count.getStreamKey(), count.getTotalViewerCount());
    }
}
