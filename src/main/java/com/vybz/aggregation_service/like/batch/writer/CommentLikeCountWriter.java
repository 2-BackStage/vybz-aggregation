package com.vybz.aggregation_service.like.batch.writer;

import com.vybz.aggregation_service.kafka.event.CommentLikeCountEvent;
import com.vybz.aggregation_service.kafka.producer.CommentLikeCountKafkaProducer;
import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import com.vybz.aggregation_service.like.infrastructure.CommentLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentLikeCountWriter implements ItemWriter<CommentLikeCount> {

    private final CommentLikeCountRepository commentLikeCountRepository;
    private final CommentLikeCountKafkaProducer commentLikeCountKafkaProducer;

    @Override
    public void write(Chunk<? extends CommentLikeCount> items) {
        for (CommentLikeCount newCount : items) {
            commentLikeCountRepository.findByCommentId(newCount.getCommentId())
                    .ifPresentOrElse(existing -> {
                        int before = existing.getTotalLikeCount();
                        int after = newCount.getTotalLikeCount();

                        existing.overwrite(newCount);
                        commentLikeCountRepository.save(existing);

                        if (before != after) {
                            sendEvent(newCount);
                        }
                    }, () -> {
                        if (newCount.getTotalLikeCount() > 0) {
                            commentLikeCountRepository.save(newCount);
                            sendEvent(newCount);
                        }
                    });
        }
    }

    private void sendEvent(CommentLikeCount count) {
        CommentLikeCountEvent event = CommentLikeCountEvent.builder()
                .commentId(count.getCommentId())
                .delta(count.getTotalLikeCount())
                .build();

        commentLikeCountKafkaProducer.sendCommentLikeCountEvent(event);
    }
}
