package com.vybz.aggregation_service.comment.batch.processor;

import com.vybz.aggregation_service.comment.domain.CommentCount;
import com.vybz.aggregation_service.comment.domain.FeedType;
import com.vybz.aggregation_service.comment.infrastructure.CommentCountRepository;
import com.vybz.aggregation_service.comment.batch.policy.CommentCountDisplayPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentCountProcessor implements ItemProcessor<String, CommentCount> {

    private final CommentCountRepository commentCountRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public CommentCount process(String feedId) {
        long count = mongoTemplate.count(
                Query.query(Criteria.where("feedId").is(feedId)),
                "comments"
        );
        String display = CommentCountDisplayPolicy.convert(count);

        FeedType feedType = commentCountRepository.findByFeedId(feedId)
                .map(CommentCount::getFeedType)
                .orElse(null);

        return CommentCount.builder()
                .feedId(feedId)
                .feedType(feedType)
                .totalCommentCount((int) count)
                .displayCommentCount(display)
                .updatedAt(Instant.now())
                .build();
    }
}
