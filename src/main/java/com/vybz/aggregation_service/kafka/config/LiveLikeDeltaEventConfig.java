package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.LiveLikeDeltaEvent;
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
public class LiveLikeDeltaEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;

    @Bean
    public ConsumerFactory<String, LiveLikeDeltaEvent> liveLikeDeltaEventConsumerFactory(){
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(
                        new JsonDeserializer<>(LiveLikeDeltaEvent.class, false)
                )
        );
    }

    @Bean(name = "liveLikeDeltaEventKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, LiveLikeDeltaEvent> liveLikeDeltaEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LiveLikeDeltaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(liveLikeDeltaEventConsumerFactory());
        return factory;
    }
}
