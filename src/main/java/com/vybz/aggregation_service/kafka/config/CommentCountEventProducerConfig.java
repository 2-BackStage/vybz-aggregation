package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.CommentCountEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CommentCountEventProducerConfig {

    private final CommonKafkaProducerConfig commonKafkaProducerConfig;

    @Bean
    public ProducerFactory<String, CommentCountEvent> commentCountProducerFactory() {
        Map<String, Object> configProps = commonKafkaProducerConfig.producerConfigs();
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, CommentCountEvent> commentCountKafkaTemplate() {
        return new KafkaTemplate<>(commentCountProducerFactory());
    }
}
