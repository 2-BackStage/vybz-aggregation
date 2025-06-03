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
    private int totalFollowingCount;

    @Builder
    public ResponseUserFollowingCountDto(String userUuid, int totalFollowingCount) {
        this.userUuid = userUuid;
        this.totalFollowingCount = totalFollowingCount;
    }

    public static ResponseUserFollowingCountDto from(UserFollowingCount userFollowingCount) {
        return ResponseUserFollowingCountDto.builder()
                .userUuid(userFollowingCount.getUserUuid())
                .totalFollowingCount(userFollowingCount.getTotalFollowingCount())
                .build();
    }

    public ResponseUserFollowingCountVo toVo() {
        return ResponseUserFollowingCountVo.builder()
                .userUuid(userUuid)
                .totalFollowingCount(totalFollowingCount)
                .build();
    }

}
