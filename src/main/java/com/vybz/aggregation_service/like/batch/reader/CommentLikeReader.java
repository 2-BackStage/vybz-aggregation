package com.vybz.aggregation_service.like.batch.reader;

import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentLikeReader implements ItemReader<String> {

    private final MongoTemplate mongoTemplate;
    private Iterator<String> commentIdIterator;

    @Override
    public String read() {
        if (commentIdIterator == null){
            List<String> commentIds = mongoTemplate.query(CommentLikeCount.class)
                    .distinct("commentId")
                    .as(String.class)
                    .all();
            this.commentIdIterator = commentIds.iterator();
        }
        return commentIdIterator.hasNext() ? commentIdIterator.next() : null;
    }


}
