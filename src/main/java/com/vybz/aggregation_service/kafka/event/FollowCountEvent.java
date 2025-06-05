package com.vybz.aggregation_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowCountEvent {

    private String userUuid;
    private String buskerUuid;

}
