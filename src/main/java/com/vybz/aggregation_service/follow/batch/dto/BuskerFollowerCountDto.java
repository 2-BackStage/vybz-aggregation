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
    private Integer totalFollowerCount;
    private String displayFollowerCount;
    private Instant updatedAt;

    @Builder
    public BuskerFollowerCountDto(String buskerUuid, Integer totalFollowerCount, String displayFollowerCount, Instant updatedAt) {
        this.buskerUuid = buskerUuid;
        this.totalFollowerCount = totalFollowerCount;
        this.displayFollowerCount = displayFollowerCount;
        this.updatedAt = updatedAt;
    }

    public static BuskerFollowerCountDto from(BuskerFollowerCount buskerFollowerCount) {
        return BuskerFollowerCountDto.builder()
                .buskerUuid(buskerFollowerCount.getBuskerUuid())
                .totalFollowerCount(buskerFollowerCount.getTotalFollowerCount())
                .displayFollowerCount(FollowCountDisplayPolicy.convert(buskerFollowerCount.getTotalFollowerCount()))
                .build();
    }

}
