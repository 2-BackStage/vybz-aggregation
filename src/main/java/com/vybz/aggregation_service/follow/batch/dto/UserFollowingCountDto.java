package com.vybz.aggregation_service.follow.batch.dto;

import com.vybz.aggregation_service.follow.batch.policy.FollowCountDisplayPolicy;
import com.vybz.aggregation_service.follow.domain.UserFollowingCount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class UserFollowingCountDto {

    private String userUuid;
    private Integer totalFollowingCount;
    private String displayFollowingCount;
    private Instant updatedAt;

    @Builder
    public UserFollowingCountDto(String userUuid, Integer totalFollowingCount, String displayFollowingCount, Instant updatedAt) {
        this.userUuid = userUuid;
        this.totalFollowingCount = totalFollowingCount;
        this.displayFollowingCount = displayFollowingCount;
        this.updatedAt = updatedAt;
    }

    public static UserFollowingCountDto from(UserFollowingCount userFollowingCount) {
        return UserFollowingCountDto.builder()
                .userUuid(userFollowingCount.getUserUuid())
                .totalFollowingCount(userFollowingCount.getTotalFollowingCount())
                .displayFollowingCount(FollowCountDisplayPolicy.convert(userFollowingCount.getTotalFollowingCount()))
                .updatedAt(userFollowingCount.getUpdatedAt())
                .build();
    }

}
