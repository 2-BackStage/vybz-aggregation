package com.vybz.aggregation_service.like.batch.writer;

import com.vybz.aggregation_service.kafka.event.LiveLikeCountResultEvent;
import com.vybz.aggregation_service.kafka.producer.LiveLikeCountResultEventProducer;
import com.vybz.aggregation_service.like.domain.LiveLikeCount;
import com.vybz.aggregation_service.like.infrastructure.LiveLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class LiveLikeCountWriter implements ItemWriter<LiveLikeCount> {

    private final LiveLikeRepository liveLikeRepository;
    private final LiveLikeCountResultEventProducer liveLikeCountResultEventProducer;

    @Override
    public void write(Chunk<? extends LiveLikeCount> items) {
        log.info("🖊️ Writer 시작 - 총 {}건 처리 예정", items.size());

        for (LiveLikeCount newCount : items) {
            String streamKey = newCount.getStreamKey();
            Integer incomingCount = newCount.getTotalLikeCount();
            String updatedAt = newCount.getUpdatedAt().toString();

            log.info("➡️ 처리 중: streamKey={}, incomingCount={}, updatedAt={}", streamKey, incomingCount, updatedAt);

            liveLikeRepository.findById(streamKey)
                    .ifPresentOrElse(
                            existing -> {
                                int before = existing.getTotalLikeCount();
                                int after = newCount.getTotalLikeCount();

                                log.info("🔄 기존 값 발견: streamKey={}, 기존 count={}, 새로운 count={}", streamKey, before, after);

                                LiveLikeCount updated = LiveLikeCount.builder()
                                        .id(existing.getId())
                                        .streamKey(existing.getStreamKey())
                                        .totalLikeCount(after)
                                        .updatedAt(newCount.getUpdatedAt())
                                        .build();

                                liveLikeRepository.save(updated);
                                log.info("✅ 업데이트 완료: streamKey={}, 최종 count={}, 저장 시각={}",
                                        streamKey, after, newCount.getUpdatedAt());
                                sendKafka(updated);
                            },
                            () -> {
                                liveLikeRepository.save(newCount);
                                log.info("🆕 신규 저장 완료: streamKey={}, count={}, 저장 시각={}",
                                        streamKey, incomingCount, updatedAt);
                                sendKafka(newCount);
                            }

                    );
        }

        log.info("✅ Writer 완료 - 전체 {}건 저장 처리 성공", items.size());
    }

    private void sendKafka(LiveLikeCount count) {
        LiveLikeCountResultEvent event = LiveLikeCountResultEvent.builder()
                .streamKey(count.getStreamKey())
                .totalLikeCount(count.getTotalLikeCount())
                .build();

        liveLikeCountResultEventProducer.sendLiveLikeCountEvent(event);
        log.info("📤 Kafka 전송 완료: streamKey={}, totalLikeCount={}",
                count.getStreamKey(), count.getTotalLikeCount());
    }
}
