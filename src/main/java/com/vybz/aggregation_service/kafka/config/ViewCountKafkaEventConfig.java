package com.vybz.aggregation_service.kafka.config;

import com.vybz.aggregation_service.kafka.event.ViewCountKafkaEvent;
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
public class ViewCountKafkaEventConfig {

    private final CommonKafkaConfig commonKafkaConfig;

    @Bean
    public ConsumerFactory<String, ViewCountKafkaEvent> viewCountKafkaEventConsumerFactory(){
        return new DefaultKafkaConsumerFactory<>(
                commonKafkaConfig.commonConsumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(
                        new JsonDeserializer<>(ViewCountKafkaEvent.class,false)
                )
        );
    }

    @Bean(name = "viewCountKafkaEventKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ViewCountKafkaEvent> viewCountKafkaEventConcurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, ViewCountKafkaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(viewCountKafkaEventConsumerFactory());
        return factory;
    }


}
