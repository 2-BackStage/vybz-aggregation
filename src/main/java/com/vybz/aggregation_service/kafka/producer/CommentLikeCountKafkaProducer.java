package com.vybz.aggregation_service.kafka.producer;

import com.vybz.aggregation_service.kafka.event.CommentLikeCountEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentLikeCountKafkaProducer {

    private final KafkaTemplate<String, CommentLikeCountEvent> commentLikeCountKafkaTemplate;
    private static final String TOPIC = "comment-like-count";

    public void sendCommentLikeCountEvent(CommentLikeCountEvent event) {
       commentLikeCountKafkaTemplate.send(TOPIC,event.getCommentId(),event);
    }
}
