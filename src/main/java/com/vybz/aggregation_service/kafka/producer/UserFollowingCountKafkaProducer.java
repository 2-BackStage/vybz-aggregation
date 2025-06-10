package com.vybz.aggregation_service.kafka.producer;

import com.vybz.aggregation_service.kafka.event.UserFollowingCountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFollowingCountKafkaProducer {

    private final KafkaTemplate<String, UserFollowingCountEvent> kafkaTemplate;
    private static final String TOPIC = "user-following-count";

    public void sendUserFollowingCountEvent(UserFollowingCountEvent event) {
        log.info("📦 [Kafka] 유저 팔로잉 수 이벤트 전송: {}", event);
        kafkaTemplate.send(TOPIC, event.getUserUuid(), event);
    }

}
