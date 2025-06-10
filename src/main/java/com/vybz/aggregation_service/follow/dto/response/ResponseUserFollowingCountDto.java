package com.vybz.aggregation_service.follow.dto.response;

import com.vybz.aggregation_service.follow.domain.UserFollowingCount;
import com.vybz.aggregation_service.follow.vo.response.ResponseUserFollowingCountVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseUserFollowingCountDto {

    private String userUuid;
    private int followerCount;

    @Builder
    public ResponseUserFollowingCountDto(String userUuid, int followerCount) {
        this.userUuid = userUuid;
        this.followerCount = followerCount;
    }

    public static ResponseUserFollowingCountDto from(UserFollowingCount userFollowingCount) {
        return ResponseUserFollowingCountDto.builder()
                .userUuid(userFollowingCount.getUserUuid())
                .followerCount(userFollowingCount.getFollowingCount())
                .build();
    }

    public ResponseUserFollowingCountVo toVo() {
        return ResponseUserFollowingCountVo.builder()
                .userUuid(userUuid)
                .followerCount(followerCount)
                .build();
    }

}
