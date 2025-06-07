package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.comment.batch.policy.CommentCountDisplayPolicy;
import com.vybz.aggregation_service.comment.domain.CommentCount;
import com.vybz.aggregation_service.comment.infrastructure.CommentCountRepository;
import com.vybz.aggregation_service.kafka.event.CommentCountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentCountEventConsumer {

    private static final String TOPIC = "comment-count";
    private static final String GROUP_ID = "comment-count-group";

    private final CommentCountRepository commentCountRepository;

    @KafkaListener(
            topics = TOPIC,
            groupId = GROUP_ID,
            containerFactory = "commentKafkaListenerContainerFactory"
    )
    public void consumeCommentCountEvent(CommentCountEvent commentCountEvent) {
        log.info("🔥 Kafka 댓글 생성 메시지 수신: {}", commentCountEvent);
        upsertCommentCount(commentCountEvent);
    }

    private void upsertCommentCount(CommentCountEvent event) {
        commentCountRepository.findByFeedId(event.getFeedId())
                .ifPresentOrElse(
                        existing -> updateCommentCount(existing, event),
                        () -> insertNewCommentCount(event)
                );
    }

    private void updateCommentCount(CommentCount commentCount, CommentCountEvent event) {
        commentCount.updateCount(event.getDelta());
        commentCountRepository.save(commentCount);
        log.info("✅ 댓글 수 업데이트 완료: feedId={}, count={}", commentCount.getFeedId(), commentCount.getTotalCommentCount());
    }

    private void insertNewCommentCount(CommentCountEvent event) {
        CommentCount newCount = CommentCount.builder()
                .feedId(event.getFeedId())
                .totalCommentCount(Math.max(0, event.getDelta()))
                .displayCommentCount(CommentCountDisplayPolicy.convert(event.getDelta()))
                .updatedAt(Instant.now())
                .build();

        commentCountRepository.save(newCount);
        log.info("🆕 댓글 수 새로 저장: feedId={}, count={}", newCount.getFeedId(), newCount.getTotalCommentCount());
    }
}
