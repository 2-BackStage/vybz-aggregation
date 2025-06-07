package com.vybz.aggregation_service.comment.batch.writer;

import com.vybz.aggregation_service.comment.domain.CommentCount;
import com.vybz.aggregation_service.comment.infrastructure.CommentCountRepository;
import com.vybz.aggregation_service.kafka.event.CommentCountEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@StepScope
@RequiredArgsConstructor
public class CommentCountWriter implements ItemWriter<CommentCount> {

    private final CommentCountRepository commentCountRepository;
    private final KafkaTemplate<String, CommentCountEvent> kafkaTemplate;

    @Override
    public void write(Chunk<? extends CommentCount> items) {
        for (CommentCount commentCount : items) {
            commentCountRepository.findByFeedId(commentCount.getFeedId())
                    .ifPresentOrElse(
                            existing -> {
                                existing.updateCount(commentCount.getTotalCommentCount());
                                commentCountRepository.save(existing);
                            },
                            () -> {
                                commentCountRepository.save(commentCount);
                            }
                    );
        }
    }
}

