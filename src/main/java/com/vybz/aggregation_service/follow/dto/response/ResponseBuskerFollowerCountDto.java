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
    private int totalFollowerCount;

    @Builder
    public ResponseBuskerFollowerCountDto(String buskerUuid, int totalFollowerCount) {
        this.buskerUuid = buskerUuid;
        this.totalFollowerCount = totalFollowerCount;
    }

    public static ResponseBuskerFollowerCountDto from(BuskerFollowerCount buskerFollowerCount) {
        return ResponseBuskerFollowerCountDto.builder()
                .buskerUuid(buskerFollowerCount.getBuskerUuid())
                .totalFollowerCount(buskerFollowerCount.getTotalFollowerCount())
                .build();
    }

    public ResponseBuskerFollowerCountVo toVo() {
        return ResponseBuskerFollowerCountVo.builder()
                .buskerUuid(buskerUuid)
                .totalFollowerCount(totalFollowerCount)
                .build();
    }

}
