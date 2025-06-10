package com.vybz.aggregation_service.follow.batch.writer;

import com.vybz.aggregation_service.follow.batch.dto.BuskerFollowerCountDto;
import com.vybz.aggregation_service.kafka.event.BuskerFollowerCountEvent;
import com.vybz.aggregation_service.kafka.producer.BuskerFollowerCountKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuskerFollowerCountWriter implements ItemWriter<BuskerFollowerCountDto> {

    private final BuskerFollowerCountKafkaProducer buskerFollowerCountKafkaProducer;

    @Override
    public void write(Chunk<? extends BuskerFollowerCountDto> items) {
        for (BuskerFollowerCountDto item : items) {
            BuskerFollowerCountEvent event = BuskerFollowerCountEvent.builder()
                    .buskerUuid(item.getBuskerUuid())
                    .followerCount(item.getFollowerCount())
                    .displayFollowerCount(item.getDisplayFollowerCount())
                    .build();
            buskerFollowerCountKafkaProducer.sendBuskerFollowerCountEvent(event);
        }

    }
}
