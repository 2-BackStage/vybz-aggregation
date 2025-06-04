package com.vybz.aggregation_service.follow.application;

import com.vybz.aggregation_service.common.entity.BaseResponseStatus;
import com.vybz.aggregation_service.exception.BaseException;
import com.vybz.aggregation_service.follow.domain.BuskerFollowerCount;
import com.vybz.aggregation_service.follow.domain.UserFollowingCount;
import com.vybz.aggregation_service.follow.dto.response.ResponseBuskerFollowerCountDto;
import com.vybz.aggregation_service.follow.dto.response.ResponseUserFollowingCountDto;
import com.vybz.aggregation_service.follow.infrastructure.BuskerFollowerCountRepository;
import com.vybz.aggregation_service.follow.infrastructure.UserFollowingCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowCountServiceImpl implements FollowCountService {

    private final UserFollowingCountRepository userFollowingCountRepository;
    private final BuskerFollowerCountRepository buskerFollowerCountRepository;

    /**
     * 유저의 팔로잉 수 조회
     * @param userUuid
     */
    @Override
    public ResponseUserFollowingCountDto getUserFollowingCount(String userUuid) {
        UserFollowingCount userFollowingCount = userFollowingCountRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_USER));
        return ResponseUserFollowingCountDto.from(userFollowingCount);
    }

    /**
     * 버스커의 팔로워 수 조회
     * @param buskerUuid
     */
    @Override
    public ResponseBuskerFollowerCountDto getBuskerFollowerCount(String buskerUuid) {
        BuskerFollowerCount buskerFollowerCount = buskerFollowerCountRepository.findByBuskerUuid(buskerUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_BUSKER));
        return ResponseBuskerFollowerCountDto.from(buskerFollowerCount);
    }
}
