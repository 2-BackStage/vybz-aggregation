package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.kafka.event.CommentLikeCountEvent;
import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import com.vybz.aggregation_service.like.infrastructure.CommentLikeCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentLikeDeltaEventConsumer {

    private static final String TOPIC_NAME = "comment-like-count";
    private static final String GROUP_ID = "comment-like-count-group";

    private final CommentLikeCountRepository commentLikeCountRepository;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = GROUP_ID,
            containerFactory = "commentLikeCountKafkaListenerContainerFactory"
    )
    public void consumeCommentLikeCountEvent(CommentLikeCountEvent event) {
        if (event.getDelta() == 0) {
            log.info("🚫 [무시] delta=0 이벤트: commentId={}", event.getCommentId());
            return;
        }

        log.info("🔥 Kafka 댓글 좋아요 수 메시지 수신: {}", event);
        upsertCommentLikeCount(event);
    }




    private void updateCommentLikeCount(CommentLikeCount existing, CommentLikeCountEvent event) {
        existing.updateCount(event.getDelta());
        commentLikeCountRepository.save(existing);
        log.info("✅ 댓글 좋아요 수 업데이트 완료: commentId={}, count={}", existing.getCommentId(), existing.getTotalLikeCount());
    }

    private void upsertCommentLikeCount(CommentLikeCountEvent event) {
        commentLikeCountRepository.findByCommentId(event.getCommentId())
                .ifPresentOrElse(
                        existing -> UpdateCommentLikeCount(existing, event),
                        () -> insertNewCommentLikeCount(event)
                );
    }

    private void UpdateCommentLikeCount(CommentLikeCount existing, CommentLikeCountEvent event) {
        existing.updateCount(event.getDelta());
        commentLikeCountRepository.save(existing);
        log.info("✅ 댓글 좋아요 수 업데이트 완료: commentId={}, count={}", existing.getCommentId(), existing.getTotalLikeCount());
    }

    private void insertNewCommentLikeCount(CommentLikeCountEvent event) {
        CommentLikeCount newCount = CommentLikeCount.builder()
                .commentId(event.getCommentId())
                .totalLikeCount(Math.max(0, event.getDelta()))
                .updatedAt(Instant.now())
                .build();

        commentLikeCountRepository.save(newCount);
        log.info("🆕 댓글 좋아요 수 새로 저장: commentId={}, count={}", newCount.getCommentId(), newCount.getTotalLikeCount());
    }
}
