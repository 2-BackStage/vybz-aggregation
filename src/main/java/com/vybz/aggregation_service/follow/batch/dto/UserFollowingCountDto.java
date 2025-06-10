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
    private Integer followingCount;
    private String displayFollowingCount;
    private Instant updatedAt;

    @Builder
    public UserFollowingCountDto(String userUuid, Integer followingCount, String displayFollowingCount, Instant updatedAt) {
        this.userUuid = userUuid;
        this.followingCount = followingCount;
        this.displayFollowingCount = displayFollowingCount;
        this.updatedAt = updatedAt;
    }

    public static UserFollowingCountDto from(UserFollowingCount userFollowingCount) {
        return UserFollowingCountDto.builder()
                .userUuid(userFollowingCount.getUserUuid())
                .followingCount(userFollowingCount.getFollowingCount())
                .displayFollowingCount(FollowCountDisplayPolicy.convert(userFollowingCount.getFollowingCount()))
                .updatedAt(userFollowingCount.getUpdatedAt())
                .build();
    }

}
