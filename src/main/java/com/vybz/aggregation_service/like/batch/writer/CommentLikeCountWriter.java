package com.vybz.aggregation_service.like.batch.writer;

import com.vybz.aggregation_service.kafka.event.CommentLikeCountEvent;
import com.vybz.aggregation_service.kafka.producer.CommentLikeCountKafkaProducer;
import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import com.vybz.aggregation_service.like.infrastructure.CommentLikeCountRepository;
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
public class CommentLikeCountWriter implements ItemWriter<CommentLikeCount> {

    private final CommentLikeCountRepository commentLikeCountRepository;
    private final CommentLikeCountKafkaProducer commentLikeCountKafkaProducer;

    @Override
    public void write(Chunk<? extends CommentLikeCount> items) {
        for (CommentLikeCount newCount : items) {
            try {
                commentLikeCountRepository.findByCommentId(newCount.getCommentId())
                        .ifPresentOrElse(existing -> {
                            int before = existing.getTotalLikeCount();
                            int after = newCount.getTotalLikeCount();

                            log.info("🔁 기존 댓글 좋아요 집계 업데이트: commentId={}, before={}, after={}",
                                    newCount.getCommentId(), before, after);

                            existing.overwrite(newCount);
                            commentLikeCountRepository.save(existing);

                            if (before != after) {
                                log.info("📤 좋아요 수 변경 감지 → Kafka 이벤트 발행: commentId={}, likeCount={}",
                                        newCount.getCommentId(), after);
                                sendEvent(existing);
                            } else {
                                log.info("⏭ 좋아요 수 변경 없음: commentId={}", newCount.getCommentId());
                            }
                        }, () -> {
                            if (newCount.getTotalLikeCount() > 0) {
                                commentLikeCountRepository.save(newCount);
                                log.info("🆕 신규 댓글 좋아요 집계 저장 및 이벤트 발행: commentId={}, likeCount={}",
                                        newCount.getCommentId(), newCount.getTotalLikeCount());
                                sendEvent(newCount);
                            } else {
                                log.info("❌ 좋아요 수 0 → 저장 및 이벤트 생략: commentId={}", newCount.getCommentId());
                            }
                        });
            } catch (Exception e) {
                log.error("🔥 댓글 좋아요 집계 처리 중 예외 발생: commentId={}, error={}",
                        newCount.getCommentId(), e.getMessage(), e);
            }
        }
    }

    private void sendEvent(CommentLikeCount count) {
        CommentLikeCountEvent event = CommentLikeCountEvent.builder()
                .commentId(count.getCommentId())
                .totalLikeCount(count.getTotalLikeCount())
                .build();

        commentLikeCountKafkaProducer.sendCommentLikeCountEvent(event);
    }
}
