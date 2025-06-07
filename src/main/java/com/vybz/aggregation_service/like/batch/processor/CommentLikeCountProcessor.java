package com.vybz.aggregation_service.like.batch.processor;

import com.vybz.aggregation_service.like.batch.policy.CommentLikeCountDisplayPolicy;
import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import com.vybz.aggregation_service.like.infrastructure.CommentLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentLikeCountProcessor implements ItemProcessor<String, CommentLikeCount> {

    private final CommentLikeCountRepository commentLikeCountRepository;

    @Override
    public CommentLikeCount process(String commentId){
        int count = (int) commentLikeCountRepository.countByCommentId(commentId);

        String display = CommentLikeCountDisplayPolicy.convert(count);

        return CommentLikeCount.builder()
                .commentId(commentId)
                .totalLikeCount(count)
                .displayLikeCount(display)
                .updatedAt(Instant.now())
                .build();
    }
}
