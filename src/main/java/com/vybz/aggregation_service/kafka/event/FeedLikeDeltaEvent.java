package com.vybz.aggregation_service.kafka.event;


import com.vybz.aggregation_service.like.domain.FeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedLikeDeltaEvent {

    private String feedId;
    private FeedType feedType;
    private int delta;
}
