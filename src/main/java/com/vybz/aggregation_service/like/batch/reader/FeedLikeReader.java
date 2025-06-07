package com.vybz.aggregation_service.like.batch.reader;

import com.vybz.aggregation_service.like.domain.FeedLikeCount;
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
public class FeedLikeReader implements ItemReader<String> {

    private final MongoTemplate mongoTemplate;
    private Iterator<String> feedIdIterator;

    @Override
    public String read(){
        if (feedIdIterator == null){
            List<String> feedIds = mongoTemplate.query(FeedLikeCount.class)
                    .distinct("feedId")
                    .as(String.class)
                    .all();
            this.feedIdIterator = feedIds.iterator();
        }
        return feedIdIterator.hasNext() ? feedIdIterator.next() : null;
    }
}
