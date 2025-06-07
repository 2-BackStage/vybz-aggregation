package com.vybz.aggregation_service.like.batch.writer;

import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import com.vybz.aggregation_service.like.infrastructure.CommentLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentLikeCountWriter implements ItemWriter<CommentLikeCount> {

    private final KafkaTemplate<String, CommentLikeCount> kafkaTemplate;
    private final CommentLikeCountRepository commentLikeCountRepository;

    @Override
    public void write(Chunk<? extends CommentLikeCount> items){
        for (CommentLikeCount commentLikeCount : items){
            commentLikeCountRepository.findByCommentId(commentLikeCount.getCommentId())
                .ifPresentOrElse(
                    existing -> {
                        existing.updateCount(commentLikeCount.getTotalLikeCount());
                        commentLikeCountRepository.save(existing);
                    },
                    () -> {
                        commentLikeCountRepository.save(commentLikeCount);
                    }
                );
        }

    }
}
