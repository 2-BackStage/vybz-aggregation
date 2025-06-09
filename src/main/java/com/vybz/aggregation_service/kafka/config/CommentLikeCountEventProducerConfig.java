package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.CommentLikeCountEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CommentLikeCountEventProducerConfig {

    private final CommonKafkaProducerConfig commonKafkaProducerConfig;

    @Bean
    public ProducerFactory<String, CommentLikeCountEvent> commentLikeCountProducerFactory() {
        Map<String, Object> configProps = commonKafkaProducerConfig.producerConfigs();
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, CommentLikeCountEvent> commentLikeCountKafkaTemplate() {
        return new KafkaTemplate<>(commentLikeCountProducerFactory());
    }
}
