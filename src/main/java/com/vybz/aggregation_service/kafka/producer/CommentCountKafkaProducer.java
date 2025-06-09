package com.vybz.aggregation_service.kafka.producer;

import com.vybz.aggregation_service.kafka.event.CommentCountEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCountKafkaProducer {

    private final KafkaTemplate<String, CommentCountEvent> commentCountKafkaTemplate;
    private static final String TOPIC = "comment-count";

    public void sendCommentCountEvent(CommentCountEvent event) {
        commentCountKafkaTemplate.send(TOPIC, event.getFeedId(), event);
    }
}
