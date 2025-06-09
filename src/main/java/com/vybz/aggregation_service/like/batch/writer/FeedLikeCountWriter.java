package com.vybz.aggregation_service.like.batch.writer;

import com.vybz.aggregation_service.kafka.event.FeedLikeCountResultEvent;
import com.vybz.aggregation_service.kafka.producer.FeedLikeCountResultEventProducer;
import com.vybz.aggregation_service.like.domain.FeedLikeCount;
import com.vybz.aggregation_service.like.infrastructure.FeedLikeCountRepository;
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
public class FeedLikeCountWriter implements ItemWriter<FeedLikeCount> {

    private final FeedLikeCountRepository feedLikeCountRepository;
    private final FeedLikeCountResultEventProducer feedLikeCountResultEventProducer;

    @Override
    public void write(Chunk<? extends FeedLikeCount> items) {
        for (FeedLikeCount newCount : items) {
            log.info("🔍 write 진입: feedId={}, count={}", newCount.getFeedId(), newCount.getTotalLikeCount());

            feedLikeCountRepository.findByFeedId(newCount.getFeedId())
                    .ifPresentOrElse(
                            existing -> {
                                int before = existing.getTotalLikeCount();
                                int after = newCount.getTotalLikeCount();

                                existing.overwrite(newCount);
                                feedLikeCountRepository.save(existing);


                                sendEvent(existing);

                                log.info("📤 Kafka 전송 완료: feedId={}, before={}, after={}",
                                        existing.getFeedId(), before, after);
                            },
                            () -> {
                                feedLikeCountRepository.save(newCount);
                                sendEvent(newCount);

                                log.info("🆕 신규 저장 및 Kafka 전송: feedId={}, count={}",
                                        newCount.getFeedId(), newCount.getTotalLikeCount());
                            }
                    );
        }
    }

    private void sendEvent(FeedLikeCount count) {
        FeedLikeCountResultEvent event = FeedLikeCountResultEvent.builder()
                .feedId(count.getFeedId())
                .feedType(count.getFeedType())
                .totalLikeCount(count.getTotalLikeCount())
                .displayLikeCount(count.getDisplayLikeCount())
                .build();

        feedLikeCountResultEventProducer.sendFeedLikeCountEvent(event);
    }
}
