package com.vybz.aggregation_service.follow.batch.dto;

import com.vybz.aggregation_service.follow.batch.policy.FollowCountDisplayPolicy;
import com.vybz.aggregation_service.follow.domain.BuskerFollowerCount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class BuskerFollowerCountDto {

    private String buskerUuid;
    private Integer followerCount;
    private String displayFollowerCount;
    private Instant updatedAt;

    @Builder
    public BuskerFollowerCountDto(String buskerUuid, Integer followerCount, String displayFollowerCount, Instant updatedAt) {
        this.buskerUuid = buskerUuid;
        this.followerCount = followerCount;
        this.displayFollowerCount = displayFollowerCount;
        this.updatedAt = updatedAt;
    }

    public static BuskerFollowerCountDto from(BuskerFollowerCount buskerFollowerCount) {
        return BuskerFollowerCountDto.builder()
                .buskerUuid(buskerFollowerCount.getBuskerUuid())
                .followerCount(buskerFollowerCount.getFollowerCount())
                .displayFollowerCount(FollowCountDisplayPolicy.convert(buskerFollowerCount.getFollowerCount()))
                .build();
    }

}
