package com.vybz.aggregation_service.follow.application;

import com.vybz.aggregation_service.follow.dto.response.ResponseBuskerFollowerCountDto;
import com.vybz.aggregation_service.follow.dto.response.ResponseUserFollowingCountDto;

public interface FollowCountService {

    /**
     * 사용자 팔로잉 수 조회
     * @param userUuid
     */
    ResponseUserFollowingCountDto getUserFollowingCount(String userUuid);

    /**
     * 버스커 팔로워 수 조회
     * @param buskerUuid
     */
    ResponseBuskerFollowerCountDto getBuskerFollowerCount(String buskerUuid);

}
