package com.vybz.aggregation_service.comment.batch.writer;

import com.vybz.aggregation_service.comment.domain.CommentCount;
import com.vybz.aggregation_service.comment.infrastructure.CommentCountRepository;
import com.vybz.aggregation_service.kafka.event.CommentCountEvent;
import com.vybz.aggregation_service.kafka.producer.CommentCountKafkaProducer;
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
public class CommentCountWriter implements ItemWriter<CommentCount> {

    private final CommentCountRepository commentCountRepository;
    private final CommentCountKafkaProducer commentCountKafkaProducer;

    @Override
    public void write(Chunk<? extends CommentCount> items) {
        for (CommentCount commentCount : items) {
            log.info("🔍 write 진입: feedId={}, count={}", commentCount.getFeedId(), commentCount.getTotalCommentCount());

            commentCountRepository.findByFeedId(commentCount.getFeedId())
                    .ifPresentOrElse(
                            existing -> {
                                log.info("🔁 기존 존재: feedId={}, 기존 count={}, 새로운 count={}",
                                        existing.getFeedId(), existing.getTotalCommentCount(), commentCount.getTotalCommentCount());

                                existing.setCount(commentCount.getTotalCommentCount());
                                commentCountRepository.save(existing);

                                commentCountKafkaProducer.sendCommentCountEvent(
                                        CommentCountEvent.builder()
                                                .feedId(existing.getFeedId())
                                                .feedType(existing.getFeedType())
                                                .totalCount(existing.getTotalCommentCount())
                                                .build()
                                );

                                log.info("📤 Kafka 전송 완료: feedId={}, count={}", existing.getFeedId(), existing.getTotalCommentCount());
                            },
                            () -> {
                                log.info("🆕 신규 저장 및 Kafka 전송: feedId={}, count={}",
                                        commentCount.getFeedId(), commentCount.getTotalCommentCount());

                                commentCountRepository.save(commentCount);

                                commentCountKafkaProducer.sendCommentCountEvent(
                                        CommentCountEvent.builder()
                                                .feedId(commentCount.getFeedId())
                                                .feedType(commentCount.getFeedType())
                                                .totalCount(commentCount.getTotalCommentCount())
                                                .build()
                                );
                            }
                    );
        }
    }
}
