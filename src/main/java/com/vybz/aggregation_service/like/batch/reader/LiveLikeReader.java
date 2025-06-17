package com.vybz.aggregation_service.like.batch.reader;

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
public class LiveLikeReader implements ItemReader<String> {

    private final MongoTemplate mongoTemplate;
    private Iterator<String> streamKeyIterator;

    @Override
    public String read() {
        if (streamKeyIterator == null) {
            List<String> streamKeys = mongoTemplate
                    .getCollection("live_like_count")
                    .distinct("streamKey", String.class)
                    .into(new java.util.ArrayList<>());

            this.streamKeyIterator = streamKeys.iterator();
        }

        return streamKeyIterator.hasNext() ? streamKeyIterator.next() : null;
    }
}
