package com.vybz.aggregation_service.like.batch.processor;

import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import com.vybz.aggregation_service.like.infrastructure.CommentLikeCountRepository;
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
public class CommentLikeCountProcessor implements ItemProcessor<String, CommentLikeCount> {

    private final MongoTemplate mongoTemplate;

    @Override
    public CommentLikeCount process(String commentId) {

        long count = mongoTemplate.count(
                Query.query(Criteria.where("commentId").is(commentId)),
                "comment_likes"
        );
        return CommentLikeCount.builder()
                .commentId(commentId)
                .totalLikeCount((int) count)
                .updatedAt(Instant.now())
                .build();
    }
}