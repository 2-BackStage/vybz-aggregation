package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.FeedLikeDeltaEvent;
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
public class FeedLikeDeltaEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;

    @Bean
    public ConsumerFactory<String, FeedLikeDeltaEvent> feedLikeDeltaEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(
                        new JsonDeserializer<>(FeedLikeDeltaEvent.class, false)
                )
        );
    }

    @Bean(name = "feedLikeDeltaEventConcurrentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, FeedLikeDeltaEvent> feedLikeDeltaEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FeedLikeDeltaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(feedLikeDeltaEventConsumerFactory());
        return factory;
    }
}
