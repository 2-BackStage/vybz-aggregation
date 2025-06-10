package com.vybz.aggregation_service.follow.vo.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseUserFollowingCountVo {

    private String userUuid;
    private int followerCount;

    @Builder
    public ResponseUserFollowingCountVo(String userUuid, int followerCount) {
        this.userUuid = userUuid;
        this.followerCount = followerCount;
    }

}
