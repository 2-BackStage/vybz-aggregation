package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.FollowCountEvent;
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
public class FollowCountEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;

    @Bean
    public ConsumerFactory<String, FollowCountEvent> followCountEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(FollowCountEvent.class, false))
        );
    }

    @Bean(name = "followKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, FollowCountEvent> followKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FollowCountEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(followCountEventConsumerFactory());

        return factory;
    }

}
