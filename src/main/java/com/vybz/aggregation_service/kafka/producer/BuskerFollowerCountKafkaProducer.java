package com.vybz.aggregation_service.kafka.producer;

import com.vybz.aggregation_service.kafka.event.BuskerFollowerCountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuskerFollowerCountKafkaProducer {

    private final KafkaTemplate<String, BuskerFollowerCountEvent> kafkaTemplate;
    private static final String TOPIC = "busker-follower-count";

    public void sendBuskerFollowerCountEvent(BuskerFollowerCountEvent event) {
        log.info("📦 [Kafka] 버스커 팔로워 수 이벤트 전송: {}", event);
        kafkaTemplate.send(TOPIC, event.getBuskerUuid(), event);
    }

}
