package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.comment.batch.policy.CommentCountDisplayPolicy;
import com.vybz.aggregation_service.comment.domain.CommentCount;
import com.vybz.aggregation_service.comment.infrastructure.CommentCountRepository;
import com.vybz.aggregation_service.kafka.event.CommentDeltaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentDeltaEventConsumer {

    private static final String TOPIC = "comment-delta";
    private static final String GROUP_ID = "comment-delta-group";

    private final CommentCountRepository commentCountRepository;
    private final MongoTemplate mongoTemplate;

    @KafkaListener(
            topics = TOPIC,
            groupId = GROUP_ID,
            containerFactory = "commentKafkaListenerContainerFactory"
    )
    public void consumeCommentDeltaEvent(CommentDeltaEvent event) {
        log.info("🔥 Kafka 댓글 생성 메시지 수신: {}", event);

        // 🔍 댓글 존재 여부 확인 (삭제된 댓글이라면 무시)
        boolean commentExists = mongoTemplate.exists(
                org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("_id").is(event.getCommentId())
                ),
                "comments"
        );

        if (!commentExists) {
            log.warn("❌ 존재하지 않는 댓글 → count 반영 생략: commentId={}, feedId={}, delta={}",
                    event.getCommentId(), event.getFeedId(), event.getDelta());
            return;
        }

        upsertCommentCount(event);
    }

    private void upsertCommentCount(CommentDeltaEvent event) {
        commentCountRepository.findByFeedId(event.getFeedId())
                .ifPresentOrElse(
                        existing -> updateCommentCount(existing, event),
                        () -> insertNewCommentCount(event)
                );
    }

    private void updateCommentCount(CommentCount commentCount, CommentDeltaEvent event) {
        commentCount.updateCount(event.getDelta());
        commentCountRepository.save(commentCount);
        log.info("🔄 댓글 수 업데이트 완료: feedId={}, 새로운 count={}",
                commentCount.getFeedId(), commentCount.getTotalCommentCount());
    }

    private void insertNewCommentCount(CommentDeltaEvent event) {
        CommentCount newCount = CommentCount.builder()
                .feedId(event.getFeedId())
                .feedType(event.getFeedType())
                .totalCommentCount(Math.max(0, event.getDelta()))
                .displayCommentCount(CommentCountDisplayPolicy.convert(event.getDelta()))
                .updatedAt(Instant.now())
                .build();

        commentCountRepository.save(newCount);
        log.info("🆕 댓글 수 새로 저장: feedId={}, count={}", newCount.getFeedId(), newCount.getTotalCommentCount());
    }
}
