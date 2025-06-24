package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.LiveViewCountResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class LiveViewCountResultEventProducerConfig {

    private final CommonKafkaProducerConfig commonKafkaProducerConfig;

    @Bean
    public ProducerFactory<String, LiveViewCountResultEvent> liveViewCountResultEventProducerFactory() {
        Map<String, Object> producerConfigs = commonKafkaProducerConfig.producerConfigs();
        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }

    @Bean
    public KafkaTemplate<String, LiveViewCountResultEvent> liveViewCountResultEventKafkaTemplate() {
        return new KafkaTemplate<>(liveViewCountResultEventProducerFactory());
    }
}
