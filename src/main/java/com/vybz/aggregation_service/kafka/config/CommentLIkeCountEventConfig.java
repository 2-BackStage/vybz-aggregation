package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.CommentLikeCountEvent;
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
public class CommentLIkeCountEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;

    @Bean
    public ConsumerFactory<String, CommentLikeCountEvent> commentLikeCountEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(
                        new JsonDeserializer<>(CommentLikeCountEvent.class, false)
                )
        );
    }

    @Bean(name = "commentLikeCountKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, CommentLikeCountEvent> commentLikeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CommentLikeCountEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commentLikeCountEventConsumerFactory());
        return factory;
    }
}
