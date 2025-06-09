package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.CommentCountEvent;
import com.vybz.aggregation_service.kafka.event.CommentDeltaEvent;
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
public class CommentDeltaEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;
    @Bean
    public ConsumerFactory<String, CommentDeltaEvent> CommentDeltaEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(
                        new JsonDeserializer<>(CommentDeltaEvent.class, false)
                )
        );
    }

    @Bean(name = "commentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, CommentDeltaEvent> commentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CommentDeltaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(CommentDeltaEventConsumerFactory());
        return factory;
    }
}
