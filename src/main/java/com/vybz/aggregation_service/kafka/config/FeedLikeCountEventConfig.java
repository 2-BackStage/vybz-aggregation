package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.FeedLikeCountEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@Configuration
@RequiredArgsConstructor
public class FeedLikeCountEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;

    @Bean
    public ConsumerFactory<String, FeedLikeCountEvent> feedLikeCountEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(
                        new JsonDeserializer<>(FeedLikeCountEvent.class, false)
                )
        );
    }

    @Bean(name = "feedLikeCountKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, FeedLikeCountEvent> feedLikeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FeedLikeCountEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(feedLikeCountEventConsumerFactory());
        return factory;
    }
}
