package com.vybz.aggregation_service.follow.batch.writer;

import com.vybz.aggregation_service.follow.batch.dto.UserFollowingCountDto;
import com.vybz.aggregation_service.kafka.event.UserFollowingCountEvent;
import com.vybz.aggregation_service.kafka.producer.UserFollowingCountKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFollowingCountWriter implements ItemWriter<UserFollowingCountDto> {

    private final UserFollowingCountKafkaProducer userFollowingCountKafkaProducer;

    @Override
    public void write(Chunk<? extends UserFollowingCountDto> items) {
        for(UserFollowingCountDto item : items) {
            UserFollowingCountEvent event = UserFollowingCountEvent.builder()
                    .userUuid(item.getUserUuid())
                    .followingCount(item.getFollowingCount())
                    .displayFollowingCount(item.getDisplayFollowingCount())
                    .build();
            userFollowingCountKafkaProducer.sendUserFollowingCountEvent(event);
        }
    }

}
