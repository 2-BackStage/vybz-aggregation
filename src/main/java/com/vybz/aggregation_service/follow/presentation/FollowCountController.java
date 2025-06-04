package com.vybz.aggregation_service.follow.presentation;

import com.vybz.aggregation_service.common.entity.BaseResponseEntity;
import com.vybz.aggregation_service.follow.application.FollowCountService;
import com.vybz.aggregation_service.follow.dto.response.ResponseBuskerFollowerCountDto;
import com.vybz.aggregation_service.follow.dto.response.ResponseUserFollowingCountDto;
import com.vybz.aggregation_service.follow.vo.response.ResponseBuskerFollowerCountVo;
import com.vybz.aggregation_service.follow.vo.response.ResponseUserFollowingCountVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow-count")
public class FollowCountController {

    private final FollowCountService followCountService;

    /**
     * 유저 uuid로 팔로잉 수 조회 API
     * @param userUuid
     */
    @Operation(summary = "유저 uuid로 팔로잉 수 조회 API", description = "유저 uuid로 팔로잉 수 조회 API 입니다.", tags = {"Follow-Service"})
    @GetMapping("/user/{userUuid}")
    public BaseResponseEntity<ResponseUserFollowingCountVo> getUserFollowingCount(@PathVariable("userUuid") String userUuid) {
        ResponseUserFollowingCountDto responseUserFollowingCountDto = followCountService.getUserFollowingCount(userUuid);
        return new BaseResponseEntity<>(responseUserFollowingCountDto.toVo());
    }

    /**
     * 버스커 uuid로 팔로워 수 조회 API
     * @param buskerUuid
     */
    @Operation(summary = "버스커 uuid로 팔로워 수 조회 API", description = "버스커 uuid로 팔로워 수 조회 API 입니다.", tags = {"Follow-Service"})
    @GetMapping("/busker/{buskerUuid}")
    public BaseResponseEntity<ResponseBuskerFollowerCountVo> getBuskerFollowerCount(@PathVariable("buskerUuid") String buskerUuid) {
        ResponseBuskerFollowerCountDto responseBuskerFollowerCountDto = followCountService.getBuskerFollowerCount(buskerUuid);
        return new BaseResponseEntity<>(responseBuskerFollowerCountDto.toVo());
    }

}
