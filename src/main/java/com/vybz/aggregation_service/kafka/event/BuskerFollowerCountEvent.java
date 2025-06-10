package com.vybz.aggregation_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuskerFollowerCountEvent {

    private String buskerUuid;
    private Integer followerCount;
    private String displayFollowerCount;

}
