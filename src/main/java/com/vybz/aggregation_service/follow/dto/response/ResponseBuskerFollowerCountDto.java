package com.vybz.aggregation_service.follow.dto.response;

import com.vybz.aggregation_service.follow.domain.BuskerFollowerCount;
import com.vybz.aggregation_service.follow.vo.response.ResponseBuskerFollowerCountVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseBuskerFollowerCountDto {

    private String buskerUuid;
    private int followerCount;

    @Builder
    public ResponseBuskerFollowerCountDto(String buskerUuid, int followerCount) {
        this.buskerUuid = buskerUuid;
        this.followerCount = followerCount;
    }

    public static ResponseBuskerFollowerCountDto from(BuskerFollowerCount buskerFollowerCount) {
        return ResponseBuskerFollowerCountDto.builder()
                .buskerUuid(buskerFollowerCount.getBuskerUuid())
                .followerCount(buskerFollowerCount.getFollowerCount())
                .build();
    }

    public ResponseBuskerFollowerCountVo toVo() {
        return ResponseBuskerFollowerCountVo.builder()
                .buskerUuid(buskerUuid)
                .followerCount(followerCount)
                .build();
    }

}
