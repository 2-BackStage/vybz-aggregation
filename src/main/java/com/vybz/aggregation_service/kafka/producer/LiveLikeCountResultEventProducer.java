package com.vybz.aggregation_service.kafka.producer;

import com.vybz.aggregation_service.kafka.event.LiveLikeCountResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LiveLikeCountResultEventProducer {

    private final KafkaTemplate<String, LiveLikeCountResultEvent> liveLikeCountResultEventKafkaTemplate;
    private static final String TOPIC = "live-like-count";

    public void sendLiveLikeCountEvent(LiveLikeCountResultEvent event) {
        liveLikeCountResultEventKafkaTemplate.send(TOPIC, event.getStreamKey(), event);
    }
}
