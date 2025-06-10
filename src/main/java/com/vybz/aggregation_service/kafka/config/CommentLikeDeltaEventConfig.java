package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.CommentLikeDeltaEvent;
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
public class CommentLikeDeltaEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;

    @Bean
    public ConsumerFactory<String, CommentLikeDeltaEvent> commentLikeDeltaEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(
                        new JsonDeserializer<>(CommentLikeDeltaEvent.class, false)
                )
        );
    }

    @Bean(name = "commentLikeCountKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, CommentLikeDeltaEvent> commentLikeDeltaEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CommentLikeDeltaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commentLikeDeltaEventConsumerFactory());
        return factory;
    }
}
