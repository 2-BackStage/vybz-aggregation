package com.vybz.aggregation_service.comment.batch.processor;

import com.vybz.aggregation_service.comment.domain.CommentCount;
import com.vybz.aggregation_service.comment.infrastructure.CommentCountRepository;
import com.vybz.aggregation_service.comment.batch.policy.CommentCountDisplayPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentCountProcessor implements ItemProcessor<String, CommentCount> {

    private final CommentCountRepository commentCountRepository;

    @Override
    public CommentCount process(String feedId) {

        int count = (int) commentCountRepository.countByFeedId(feedId);

        String display = CommentCountDisplayPolicy.convert(count);

        return CommentCount.builder()
                .feedId(feedId)
                .totalCommentCount(count)
                .displayCommentCount(display)
                .updatedAt(Instant.now())
                .build();
    }
}
