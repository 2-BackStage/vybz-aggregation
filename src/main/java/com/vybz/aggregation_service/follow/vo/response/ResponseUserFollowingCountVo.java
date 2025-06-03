package com.vybz.aggregation_service.follow.vo.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseUserFollowingCountVo {

    private String userUuid;
    private int totalFollowingCount;

    @Builder
    public ResponseUserFollowingCountVo(String userUuid, int totalFollowingCount) {
        this.userUuid = userUuid;
        this.totalFollowingCount = totalFollowingCount;
    }

}
