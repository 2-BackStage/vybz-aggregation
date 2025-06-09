package com.vybz.aggregation_service.kafka.producer;

import com.vybz.aggregation_service.kafka.event.FeedLikeCountResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedLikeCountResultEventProducer {

    private final KafkaTemplate<String, FeedLikeCountResultEvent> feedLikeCountResultEventKafkaTemplate;
    private static final String TOPIC = "feed-like-count";

    public void sendFeedLikeCountEvent(FeedLikeCountResultEvent event) {
        feedLikeCountResultEventKafkaTemplate.send(TOPIC, event.getFeedId(), event);
    }
}
