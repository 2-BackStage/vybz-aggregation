package com.vybz.aggregation_service.kafka.producer;

import com.vybz.aggregation_service.kafka.event.LiveViewCountResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveViewCountResultEventProducer {

    private final KafkaTemplate<String, LiveViewCountResultEvent> liveViewCountResultEventKafkaTemplate;
    private static final String TOPIC = "live-view-count-result";

    public void sendLiveViewCountEvent(LiveViewCountResultEvent event) {
        log.info("📤 Kafka 전송 시작: topic='{}', streamKey={}, totalViewerCount={}",
                TOPIC, event.getStreamKey(), event.getTotalViewerCount());

        CompletableFuture<SendResult<String, LiveViewCountResultEvent>> future =
                liveViewCountResultEventKafkaTemplate.send(TOPIC, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Kafka 전송 실패: {}", ex.getMessage(), ex);
            } else {
                log.info("✅ Kafka 전송 성공: topic='{}', offset={}",
                        TOPIC, result.getRecordMetadata().offset());
            }
        });
    }
}
