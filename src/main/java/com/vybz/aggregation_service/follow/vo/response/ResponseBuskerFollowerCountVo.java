package com.vybz.aggregation_service.follow.vo.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseBuskerFollowerCountVo {

    private String buskerUuid;
    private int followerCount;

    @Builder
    public ResponseBuskerFollowerCountVo(String buskerUuid, int followerCount) {
        this.buskerUuid = buskerUuid;
        this.followerCount = followerCount;
    }

}
